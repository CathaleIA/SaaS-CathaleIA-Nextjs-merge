package com.normalizar.repositoryDynamoDB;

import com.normalizar.repositoryDynamoDB.entity.MetaDataReport;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface IuseCaseDynamoDB {
    String CreateItem(MetaDataReport metaDataReport, DynamoDbClient client, String tableName);

    String CreateItem(String tenant, String poolUserId);
}
