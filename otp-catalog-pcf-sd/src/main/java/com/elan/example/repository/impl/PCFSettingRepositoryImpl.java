package com.elan.example.repository.impl;

import com.elan.example.repository.PCFSettingRepository;
import com.elan.example.repository.model.PCFSettingRecord;
import com.elan.example.xaap.TransactionGroup;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class PCFSettingRepositoryImpl implements PCFSettingRepository {

    private static final String PCF_SETTING_COLLECTION = "PCF_Setting_Collection";

    @Override
    public void register(PCFSettingRecord pcfSettingRecord) {
        TransactionGroup transactionGroup = TransactionGroup.getCurrent();
        transactionGroup.addOperation(((session, dataSourceManager) -> {
            Document document = new Document("clientId", pcfSettingRecord.getClientId())
                    .append("configName", pcfSettingRecord.getConfigName())
                    .append("value", pcfSettingRecord.getValue())
                    .append("status", pcfSettingRecord.getStatus())
                    .append("isDelete", false);

            dataSourceManager.insertDocument(session, PCF_SETTING_COLLECTION, document);
        }));
    }
}
