package com.danb.dca.product_serivce.models.po;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.danb.dca.product_serivce.utils.ConstantStrings.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class ProductPO {
    @DynamoDBAttribute(attributeName = CODE_KEY)
    private String code;
    @DynamoDBAttribute(attributeName = DESCRIPTION_KEY)
    private String description;
    @DynamoDBAttribute(attributeName = QUANTITY_KEY)
    private int quantity;
    @DynamoDBAttribute(attributeName = UNIT_PRICE_KEY)
    private double unitPrice;
    @DynamoDBAttribute(attributeName = DISCOUNT_KEY)
    private double discount;
    @DynamoDBAttribute(attributeName = TOTAL_KEY)
    private double total;
}
