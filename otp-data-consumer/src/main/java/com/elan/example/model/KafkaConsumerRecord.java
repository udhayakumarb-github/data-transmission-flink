package com.elan.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaConsumerRecord {
    private String topic;
    private String message;
}
