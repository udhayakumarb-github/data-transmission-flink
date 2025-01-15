package com.elan.example.xaap;

import com.mongodb.client.ClientSession;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionGroup {
    private static final ThreadLocal<TransactionGroup> currentTransaction = new ThreadLocal<>();
    private static final Logger logger = Logger.getLogger(TransactionGroup.class.getName());
    private final ConcurrentLinkedQueue<TransactionOperation> operationsQueue = new ConcurrentLinkedQueue<>();

    public TransactionGroup() {
        logger.info("TransactionGroup created for the current thread");
    }

    public static void withInitial() {
        currentTransaction.set(new TransactionGroup());
        logger.info("ThreadLocal<TransactionGroup> initialized");
    }

    public static void withInitial(TransactionGroup transactionGroup) {
        currentTransaction.set(transactionGroup);
        logger.info("ThreadLocal<TransactionGroup> initialized");
    }

    public static TransactionGroup getCurrent() {
        return currentTransaction.get();
    }

    public void addOperation(TransactionOperation operation) {
        operationsQueue.add(operation);
        logger.info("Operation added to transaction group");
    }

    public void executeAllOperations(ClientSession session, DataSourceManager dataSourceManager) {
        while (!operationsQueue.isEmpty()) {
            TransactionOperation operation = operationsQueue.poll();
            if (operation != null) {
                try {
                    operation.execute(session, dataSourceManager);
                    logger.info("Operation executed successfully");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Operation failed", e);
                    throw e; // Re-throw the exception to ensure transaction aborts
                }
            }
        }
    }
}
