package com.elan.example.xaap;

import com.mongodb.client.ClientSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TransactionManager {
    private static final DataSourceManager dataSourceManager = DataSourceManager.getInstance();
    private static final Set<TransactionGroup> transactionGroups = Collections.synchronizedSet(new HashSet<>());

    public synchronized TransactionGroup createNewTransactionGroup() {
        TransactionGroup transactionGroup = new TransactionGroup();
        transactionGroups.add(transactionGroup);
        return transactionGroup;
    }

    public synchronized TransactionGroup createNewTransactionGroup(TransactionGroup transactionGroup) {
        transactionGroups.add(transactionGroup);
        return transactionGroup;
    }

    public void executeTransaction() {
        try (ClientSession session = dataSourceManager.getSession()) {
            TransactionGroup transactionGroup = TransactionGroup.getCurrent();
            if (transactionGroup == null) {
                throw new IllegalStateException("TransactionGroup is not initialized. Please initialize it before calling executeTransaction.");
            }

            session.startTransaction();
            try {
                transactionGroup.executeAllOperations(session, dataSourceManager);
                session.commitTransaction();
                transactionGroups.remove(transactionGroup);
            } catch (Exception e) {
                session.abortTransaction();
                transactionGroups.remove(transactionGroup);
                e.printStackTrace();
                throw e;
            }
        }
    }
}
