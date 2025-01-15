package com.elan.example.service.impl;

import com.elan.example.repository.PCFSettingRepository;
import com.elan.example.repository.impl.PCFSettingRepositoryImpl;
import com.elan.example.service.PCFSettingInstantiationService;
import com.elan.example.service.mapper.PCFSettingMapper;
import com.elan.example.service.mapper.PCFSettingMapperImpl;
import com.elan.example.service.model.PCFSetting;
import com.elan.example.xaap.TransactionGroup;
import com.elan.example.xaap.TransactionManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PCFSettingInstantiationServiceImpl implements PCFSettingInstantiationService {

    private final PCFSettingRepository pcfSettingRepository;
    private final PCFSettingMapper pcfSettingMapper;

    public PCFSettingInstantiationServiceImpl() {
        this.pcfSettingRepository = new PCFSettingRepositoryImpl();
        this.pcfSettingMapper = new PCFSettingMapperImpl();
    }

    @Override
    public void initiatePCFSetting(final PCFSetting pcfSetting) {

        log.info("Initiate PCF setting configuration storage for clientId: {} and configName: {}", pcfSetting.getClientId(), pcfSetting.getConfigName());

        TransactionManager transactionManager = new TransactionManager();
        TransactionGroup.withInitial();

        pcfSettingRepository.register(pcfSettingMapper.toRecord(pcfSetting));

        transactionManager.executeTransaction();
        log.info("PCF Setting - Config name: {} for clientId: {} stored in database", pcfSetting.getConfigName(), pcfSetting.getClientId());
    }
}
