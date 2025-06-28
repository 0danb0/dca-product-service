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
public class PaymentInfoPO {
    @DynamoDBAttribute(attributeName = METHOD_KEY)
    private String method;
    @DynamoDBAttribute(attributeName = IBAN_KEY)
    private String iban;
    @DynamoDBAttribute(attributeName = ACCOUNT_HOLDER_KEY)
    private String accountHolder;
    @DynamoDBAttribute(attributeName = DUE_DATE_KEY)
    private String dueDate;
    @DynamoDBAttribute(attributeName = DUE_AMOUNT_KEY)
    private double dueAmount;
}
