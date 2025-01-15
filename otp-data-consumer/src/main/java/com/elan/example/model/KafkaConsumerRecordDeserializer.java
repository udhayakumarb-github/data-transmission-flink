package com.elan.example.model;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class KafkaConsumerRecordDeserializer implements KafkaRecordDeserializationSchema<KafkaConsumerRecord> {

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<KafkaConsumerRecord> out) throws IOException {
        out.collect(KafkaConsumerRecord.builder()
                .topic(record.topic())
                .message(new String(record.value()))
                .build());
    }

    @Override
    public TypeInformation<KafkaConsumerRecord> getProducedType() {
        return TypeInformation.of(KafkaConsumerRecord.class);
    }
}
