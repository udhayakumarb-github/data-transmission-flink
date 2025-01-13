package com.elan.example.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MongoConfig implements Serializable {
    private String uri;
    private String database;
    private List<String> collections;
}
