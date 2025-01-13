package com.elan.example.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KafkaConfig implements Serializable {
    private String bootstrapServers;
    private String topic;
}
