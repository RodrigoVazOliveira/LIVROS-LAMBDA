package com.serverless.configurations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDBConfiguration {

    private AmazonDynamoDB dynamoDB;
    private DynamoDBMapper dynamoDBMapper;

    public void build() {
        this.dynamoDB = AmazonDynamoDBClient.builder().build();
    }

    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public void buildDynamoDBMapper() {
        this.dynamoDBMapper = new DynamoDBMapper(this.dynamoDB);
    }

    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }
}