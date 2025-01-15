package com.elan.example.repository.model;

import lombok.Data;

@Data
public class PCFSettingRecord {
    private String clientId;
    private String configName;
    private String value;
    private String status;
}
