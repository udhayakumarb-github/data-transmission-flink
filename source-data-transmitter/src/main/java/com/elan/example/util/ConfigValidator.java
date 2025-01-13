package com.elan.example.util;

import com.elan.example.config.ServiceConfig;

public class ConfigValidator {
    public static void validateServiceConfig(ServiceConfig serviceConfig) {
        for (String serviceDomain : serviceConfig.getServiceDomains()) {
            if (serviceConfig.getSourceMongoConfig(serviceDomain) == null) {
                throw new RuntimeException("Missing MongoDB configuration for service domain: " + serviceDomain);
            }
            if (serviceConfig.getTargetKafkaConfig(serviceDomain) == null) {
                throw new RuntimeException("Missing Kafka configuration for service domain: " + serviceDomain);
            }
        }
    }
}
