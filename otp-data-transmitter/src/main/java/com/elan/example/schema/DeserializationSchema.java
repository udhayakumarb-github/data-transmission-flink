package com.elan.example.schema;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.bson.BsonDocument;
import org.apache.flink.connector.mongodb.source.reader.deserializer.MongoDeserializationSchema;

public class DeserializationSchema implements MongoDeserializationSchema<String> {

    @Override
    public String deserialize(BsonDocument document) {
        // Convert BsonDocument to String (or JSON representation of the document)
        return document.toJson();  // You can use any other method to represent it as a String
    }

    @Override
    public TypeInformation<String> getProducedType() {
        // Return the type information for the deserialized object, which is String in this case
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}
