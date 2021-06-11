package com.serverless.configurations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDBConfiguration {
    public static DynamoDBMapper build() {
        return new DynamoDBMapper(AmazonDynamoDBClient.builder().build());
    }
}