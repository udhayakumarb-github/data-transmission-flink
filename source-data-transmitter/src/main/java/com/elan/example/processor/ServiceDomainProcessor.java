package com.elan.example.processor;

import com.elan.example.config.KafkaConfig;
import com.elan.example.config.MongoConfig;
import com.elan.example.config.ServiceConfig;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.mongodb.source.MongoSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceDomainProcessor {
    private static final Logger log = LoggerFactory.getLogger(ServiceDomainProcessor.class);

    private final StreamExecutionEnvironment env;
    private final ServiceConfig serviceConfig;

    public ServiceDomainProcessor(StreamExecutionEnvironment env, ServiceConfig serviceConfig) {
        this.env = env;
        this.serviceConfig = serviceConfig;
    }

    public void processAllDomains() {
        for (String serviceDomain : serviceConfig.getServiceDomains()) {
            processDomain(serviceDomain);
        }
    }

    private void processDomain(String serviceDomain) {
        log.info("Processing service domain: {}", serviceDomain);

        MongoConfig mongoConfig = serviceConfig.getSourceMongoConfig(serviceDomain);
        KafkaConfig kafkaConfig = serviceConfig.getTargetKafkaConfig(serviceDomain);

        for (String collectionName : mongoConfig.getCollections()) {
            log.info("Processing collection: {} for service domain: {}", collectionName, serviceDomain);

            MongoSource<String> mongoSource = MongoSourceFactory.createMongoSource(mongoConfig, collectionName);
            KafkaSink<String> kafkaSink = KafkaSinkFactory.createKafkaSink(kafkaConfig);

            env.fromSource(mongoSource, WatermarkStrategy.noWatermarks(), "MongoDB-Source-" + collectionName)
                    .map(record -> {
                        log.info("Processing record: {}", record);
                        return record; // Transformation logic here
                    })
                    .sinkTo(kafkaSink);
        }
    }
}
