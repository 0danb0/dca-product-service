package com.danb.dca.product_serivce.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.danb.dca.product_serivce.models.po.InvoicePO;
import com.danb.dca.product_serivce.properties.DynamoDBProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InvoiceRepository {

    private final AmazonDynamoDB client;
    private final DynamoDBProperties dynamoDBProperties;
    private DynamoDBMapper dynamoDBMapper;

    @PostConstruct
    public void init() {
        String tableName = dynamoDBProperties.getTables().get("invoice");

        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName)
                ).build();

        this.dynamoDBMapper = new DynamoDBMapper(client, mapperConfig);
        log.info("DynamoDBMapper initialized for table: {}", tableName);
    }

    public void insert(InvoicePO invoicePO) {
        dynamoDBMapper.save(invoicePO);
    }

    public void delete(InvoicePO invoicePO) {
        dynamoDBMapper.delete(invoicePO);
    }
}
