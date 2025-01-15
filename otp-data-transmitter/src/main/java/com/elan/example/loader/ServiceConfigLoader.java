package com.elan.example.loader;

import com.elan.example.config.DomainConfig;
import com.elan.example.config.KafkaConfig;
import com.elan.example.config.MongoConfig;
import com.elan.example.config.ServiceConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceConfigLoader {

    public static ServiceConfig loadServiceConfigFromResource() {
        return loadServiceConfig("sd-config.properties");
    }

    public static ServiceConfig loadServiceConfigFromResource(String filename) {
        return loadServiceConfig(filename);
    }

    public static ServiceConfig loadServiceConfig(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException exception) {
            inputStream = ServiceConfigLoader.class.getClassLoader().getResourceAsStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load service configuration properties");
        }

        if (inputStream == null) {
            throw new RuntimeException("Failed to load service configuration properties");
        }

        Properties properties = new Properties();

        try {
            properties.load(inputStream);

            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setServiceDomains(Arrays.asList(properties.getProperty("service.domain.list").split(",")));
            Map<String, DomainConfig> domainConfigMap = new HashMap<>();
            for (String domain : serviceConfig.getServiceDomains()) {
                DomainConfig domainDetails = new DomainConfig();
                MongoConfig source = new MongoConfig();
                source.setUri(properties.getProperty(domain + ".source.mongo.uri"));
                source.setDatabase(properties.getProperty(domain + ".source.mongo.database"));
                source.setCollections(Arrays.asList(properties.getProperty(domain + ".source.mongo.collections").split(",")));
                domainDetails.setSource(source);
                MongoConfig mapper = new MongoConfig();
                mapper.setUri(properties.getProperty(domain + ".mapper.mongo.uri"));
                mapper.setDatabase(properties.getProperty(domain + ".mapper.mongo.database"));
                mapper.setCollections(Arrays.asList(properties.getProperty(domain + ".mapper.mongo.collections").split(",")));
                domainDetails.setMapper(mapper);
                KafkaConfig target = new KafkaConfig();
                target.setBootstrapServers(properties.getProperty(domain + ".target.kafka.bootstrapServers"));
                target.setTopic(properties.getProperty(domain + ".target.kafka.topic"));
                domainDetails.setTarget(target);
                domainConfigMap.put(domain, domainDetails);
            }
            serviceConfig.setDomainConfigs(domainConfigMap);

            return serviceConfig;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load service configuration properties");
        }
    }
}
