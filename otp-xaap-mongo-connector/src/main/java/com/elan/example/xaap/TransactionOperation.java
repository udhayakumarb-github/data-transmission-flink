package com.elan.example.xaap;

import com.mongodb.client.ClientSession;

@FunctionalInterface
public interface TransactionOperation {
    void execute(ClientSession session, DataSourceManager dataSourceManager);
}
