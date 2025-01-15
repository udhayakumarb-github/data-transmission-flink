package com.elan.example;

import com.elan.example.service.PCFSettingInstantiationService;
import com.elan.example.service.impl.PCFSettingInstantiationServiceImpl;
import com.elan.example.service.model.PCFSetting;

public class Application {
    public static void main(String args[]) {

        PCFSetting pcfSetting = new PCFSetting();
        pcfSetting.setClientId("1234");
        pcfSetting.setConfigName("discount");
        pcfSetting.setValue("10");
        pcfSetting.setStatus("Active");

        PCFSettingInstantiationService pcfSettingInstantiationService = new PCFSettingInstantiationServiceImpl();

        pcfSettingInstantiationService.initiatePCFSetting(pcfSetting);

    }
}
