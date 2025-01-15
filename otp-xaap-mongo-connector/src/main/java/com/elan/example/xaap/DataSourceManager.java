package com.elan.example.xaap;

import com.mongodb.client.*;
import org.bson.Document;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataSourceManager implements Serializable {
    private static volatile DataSourceManager instance;
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private DataSourceManager() {
        // Private constructor to prevent instantiation
        initialize();
    }

    public static DataSourceManager getInstance() {
        if (instance == null) {
            synchronized (DataSourceManager.class) {
                if (instance == null) {
                    instance = new DataSourceManager();
                }
            }
        }
        return instance;
    }

    private void initialize() {
        try {
            Properties properties = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("db-config.properties")) {
                if (input == null) {
                    throw new RuntimeException("Unable to find config.properties");
                }
                properties.load(input);
            }

            String uri = properties.getProperty("mongodb.uri");
            String dbName = properties.getProperty("mongodb.database");

            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(dbName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize MongoDB configuration", e);
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public void createCollection(String collectionName) {
        database.createCollection(collectionName);
    }

    public void insertDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.insertOne(document);
    }

    public void insertDocument(ClientSession session, String collectionName, Document document) {
        MongoCollection<Document> collection = getCollection(collectionName);
        collection.insertOne(session, document);
    }

    public List<Document> findDocuments(String collectionName, Document filter) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(filter).into(new ArrayList<>());
    }

    public ClientSession getSession() {
        return mongoClient.startSession();
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
