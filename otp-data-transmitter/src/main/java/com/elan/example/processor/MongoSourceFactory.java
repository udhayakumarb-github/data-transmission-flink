package com.elan.example.processor;

import com.elan.example.config.MongoConfig;
import com.elan.example.schema.DeserializationSchema;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.mongodb.source.MongoSource;
import org.apache.flink.connector.mongodb.source.enumerator.splitter.PartitionStrategy;

public class MongoSourceFactory {
    public static MongoSource<String> createMongoSource(MongoConfig mongoConfig, String collectionName) {
        return MongoSource.<String>builder()
                .setUri(mongoConfig.getUri())
                .setDatabase(mongoConfig.getDatabase())
                .setCollection(collectionName)
                .setProjectedFields("value") // Adjust as needed
                .setFetchSize(2048)
                .setLimit(10000)
                .setNoCursorTimeout(true)
                .setPartitionStrategy(PartitionStrategy.SAMPLE)
                .setPartitionSize(MemorySize.ofMebiBytes(64))
                .setSamplesPerPartition(10)
                .setDeserializationSchema(new DeserializationSchema())
                .build();
    }
}
