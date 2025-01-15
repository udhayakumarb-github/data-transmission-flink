package com.elan.example.job;

import com.elan.example.config.KafkaConfig;
import com.elan.example.model.KafkaConsumerRecord;
import com.elan.example.model.KafkaConsumerRecordDeserializer;
import com.elan.example.service.PCFSettingInstantiationService;
import com.elan.example.service.impl.PCFSettingInstantiationServiceImpl;
import com.elan.example.service.model.PCFSetting;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

@Slf4j
public class ServiceDomainDataConsumerJob {
    public static void main(String args[]) {
        log.info("Service domain data consumer flink job execution started");

        try {
            // Get Flink execution environment
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

            KafkaConfig kafkaConfig = new KafkaConfig();
            kafkaConfig.setBootstrapServers("localhost:9092");
            kafkaConfig.setTopics(Arrays.asList("dmm-data-topic","pcf-data-topic"));
            kafkaConfig.setGroupId("sd-group");

            KafkaSource<KafkaConsumerRecord> source = KafkaSource.<KafkaConsumerRecord>builder()
                    .setBootstrapServers(kafkaConfig.getBootstrapServers())
                    .setTopics(kafkaConfig.getTopics())
                    .setGroupId(kafkaConfig.getGroupId())
                    .setStartingOffsets(OffsetsInitializer.earliest())
                    .setDeserializer(new KafkaConsumerRecordDeserializer())
                    .build();
            DataStream<KafkaConsumerRecord> consumerRecordStream =  env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");


            consumerRecordStream.map(kafkaConsumerRecord -> {

                String topic = kafkaConsumerRecord.getTopic();
                String message = kafkaConsumerRecord.getMessage();

                switch (topic) {
                    case "dmm-data-topic":
                        processDMMData(message);
                        break;
                    case "pcf-data-topic":
                        processPCFData(message);
                        break;
                    default:
                        log.info("Ignored message from topic: {}", topic);
                        break;
                }
                return kafkaConsumerRecord;

            }).setParallelism(1);

            // Execute Flink job
            env.execute("MongoDB to Kafka Data Pipeline");
        } catch (Exception e) {
            log.error("Error in Flink application", e);
        }
        log.info("Service domain data consumer flink job execution completed");
    }

    private static void processPCFData(String message) {
        log.info("PCF data - {}", message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PCFSetting pcfSetting = objectMapper.readValue(message, PCFSetting.class);
            PCFSettingInstantiationService pcfSettingInstantiationService = new PCFSettingInstantiationServiceImpl();
            pcfSettingInstantiationService.initiatePCFSetting(pcfSetting);
        } catch (Exception e) {
            log.error("Failed to store message for Catalog PCF Setting");
        }
    }

    private static void processDMMData(String message) {
        log.info("DMM data - {}", message);
    }
}
