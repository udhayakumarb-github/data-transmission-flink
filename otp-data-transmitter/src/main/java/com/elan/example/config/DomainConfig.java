package com.elan.example.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class DomainConfig implements Serializable {
    private MongoConfig source;
    private MongoConfig mapper;
    private KafkaConfig target;
}
