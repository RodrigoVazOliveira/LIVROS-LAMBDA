package com.serverless.configurations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDBConfiguration {

    private AmazonDynamoDB dynamoDB;

    public void build() {
        this.dynamoDB = AmazonDynamoDBClient.builder().build();
    }

    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }
}