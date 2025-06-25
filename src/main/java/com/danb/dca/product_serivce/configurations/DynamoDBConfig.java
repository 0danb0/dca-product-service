package com.danb.dca.product_serivce.configurations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.danb.dca.product_serivce.properties.DynamoDBProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DynamoDBConfig {
    private final DynamoDBProperties dynamoDBProperties;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return getDynamoDBClient();
    }

    private AmazonDynamoDB getDynamoDBClient() {
        return AmazonDynamoDBClientBuilder.standard().withCredentials(getCredentialsProvider()).withEndpointConfiguration(getEndpointConfiguration()).build();
    }

    public AWSCredentialsProvider getCredentialsProvider() {
        AWSCredentials TEST_CREDENTIALS = new BasicAWSCredentials(dynamoDBProperties.getKeys().getAccess(), dynamoDBProperties.getKeys().getSecret());
        return new AWSStaticCredentialsProvider(TEST_CREDENTIALS);
    }

    protected AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(dynamoDBProperties.getEndpoint(), dynamoDBProperties.getRegion());
    }
}
