package com.elan.example.job;

import com.elan.example.config.ServiceConfig;
import com.elan.example.loader.ServiceConfigLoader;
import com.elan.example.processor.ServiceDomainProcessor;
import com.elan.example.util.ConfigValidator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceDomainDataTransmissionJob {

    private static final Logger log = LoggerFactory.getLogger(ServiceDomainDataTransmissionJob.class);

    public static void main(String[] args) throws Exception {
        log.info("Starting Flink application to process MongoDB and publish to Kafka");

        try {
            // Load service configuration
            ServiceConfig serviceConfig = ServiceConfigLoader.loadServiceConfigFromResource();

            // Validate configurations
            ConfigValidator.validateServiceConfig(serviceConfig);

            // Get Flink execution environment
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

            // Process each service domain
            ServiceDomainProcessor processor = new ServiceDomainProcessor(env, serviceConfig);
            processor.processAllDomains();

            // Execute Flink job
            env.execute("MongoDB to Kafka Data Pipeline");
        } catch (Exception e) {
            log.error("Error in Flink application", e);
        }
        log.info("Flink application execution completed");
    }
}
