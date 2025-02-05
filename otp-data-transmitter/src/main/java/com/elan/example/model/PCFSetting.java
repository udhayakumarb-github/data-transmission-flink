package com.elan.example.model;

import lombok.Data;

@Data
public class PCFSetting {
    private String clientId;
    private String configName;
    private String value;
    private String status;
}
