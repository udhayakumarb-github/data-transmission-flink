package com.elan.example.processor;

import com.elan.example.config.KafkaConfig;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

public class KafkaSinkFactory {
    public static KafkaSink<String> createKafkaSink(KafkaConfig kafkaConfig) {
        return KafkaSink.<String>builder()
                .setBootstrapServers(kafkaConfig.getBootstrapServers())
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setTopic(kafkaConfig.getTopic())
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .build()
                )
                .setDeliverGuarantee(org.apache.flink.connector.base.DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }
}
