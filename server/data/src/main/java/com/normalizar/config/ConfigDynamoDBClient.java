package com.normalizar.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class ConfigDynamoDBClient {
    public static DynamoDbClient clientDbClient(){
        return  DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
