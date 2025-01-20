# data-transmission-flink
Apache Flink application to read collections from MongoDB and produce into Kafka topic

# Apache Flink Application Architecture

## High-Level Design

```mermaid
graph TB
    A[Apache Flink Cluster] --> B[Job 1: Read .asam File & Store in MongoDB]
    A --> C[Job 2: Read from MongoDB & Publish to Kafka]
    A --> D[Job 3: Subscribe to Kafka & Store Grouped Data in MongoDB]

    B --> E[AsamFileReader]
    B --> F[MongoDBWriter]
    C --> G[MongoDBReader]
    C --> H[KafkaPublisher]
    D --> I[KafkaConsumer]
    D --> J[MongoDBWriter]

    E --> K[.asam File]
    F --> L[MongoDB Collections]
    G --> M[MongoDB Collections]
    H --> N[Kafka Topics]
    I --> O[Kafka Topics]
    J --> P[MongoDB Grouped Data]



