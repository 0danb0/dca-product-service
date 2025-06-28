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
public class CompanyPO {
    @DynamoDBAttribute(attributeName = NAME_KEY)
    private String name;
    @DynamoDBAttribute(attributeName = ADDRESS_KEY)
    private String address;
    @DynamoDBAttribute(attributeName = VAT_NUMBER_KEY)
    private String vatNumber;
    @DynamoDBAttribute(attributeName = FISCAL_CODE_KEY)
    private String fiscalCode;
    @DynamoDBAttribute(attributeName = EMAIL_KEY)
    private String email;
    @DynamoDBAttribute(attributeName = PHONE_KEY)
    private String phone;
}
