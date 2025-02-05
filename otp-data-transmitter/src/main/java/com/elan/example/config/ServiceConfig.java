package com.elan.example.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ServiceConfig implements Serializable {
    private List<String> serviceDomains;
    private Map<String, DomainConfig> domainConfigs;

    public MongoConfig getSourceMongoConfig(String serviceDomain) {
        DomainConfig domainConfig = domainConfigs.get(serviceDomain);
        if (domainConfig != null) {
            return domainConfig.getSource();
        }
        throw new RuntimeException("Source MongoDB Configuration Not Configured for domain: " + serviceDomain);
    }

    public KafkaConfig getTargetKafkaConfig(String serviceDomain) {
        // Retrieve the DomainConfig object for the given service domain
        DomainConfig domainConfig = domainConfigs.get(serviceDomain);

        // If the DomainConfig exists, return the KafkaConfig for the target
        if (domainConfig != null) {
            KafkaConfig kafkaConfig = domainConfig.getTarget();
            if (kafkaConfig != null) {
                return kafkaConfig;
            }
            throw new RuntimeException("Target Kafka Configuration Not Configured for domain: " + serviceDomain);
        }

        // If the domain is not configured, throw an exception
        throw new RuntimeException("Domain Configuration Not Found for domain: " + serviceDomain);
    }

    public MongoConfig getMetadataMongoConfig(String serviceDomain) {
        return null;
    }
}