package com.elan.example.service.mapper;

import com.elan.example.repository.model.PCFSettingRecord;
import com.elan.example.service.model.PCFSetting;
import org.mapstruct.Mapper;

@Mapper
public interface PCFSettingMapper {
    PCFSettingRecord toRecord(PCFSetting pcfSetting);
    PCFSetting toDomain(PCFSettingRecord pcfSettingRecord);
}
