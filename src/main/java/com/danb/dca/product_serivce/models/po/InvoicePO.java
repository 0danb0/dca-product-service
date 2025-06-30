package com.danb.dca.product_serivce.models.po;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.danb.dca.product_serivce.models.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.danb.dca.product_serivce.utils.ConstantStrings.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "${aws.dynamodb.tables.invoice}")
public class InvoicePO {
    @DynamoDBHashKey(attributeName = PK_KEY)
    private String pk;
    @DynamoDBRangeKey(attributeName = SK_KEY)
    private String sk;
    @DynamoDBAttribute(attributeName = CREATION_DATE_KEY)
    private String creationDate;
    @DynamoDBAttribute(attributeName = APPLICATION_ID_KEY)
    private String applicationId;
    @DynamoDBAttribute(attributeName = INVOICE_UUID_KEY)
    private String invoiceUuid;
    @DynamoDBAttribute(attributeName = INVOICE_NUMBER_KEY)
    private String invoiceNumber;
    @DynamoDBAttribute(attributeName = INVOICE_DATE_KEY)
    private String invoiceDate;
    @DynamoDBAttribute(attributeName = ISSUER_KEY)
    private CompanyPO issuer;
    @DynamoDBAttribute(attributeName = RECIPIENT_KEY)
    private CompanyPO recipient;
    @DynamoDBAttribute(attributeName = ITEMS_KEY)
    private List<ProductPO> items;
    @DynamoDBAttribute(attributeName = PAYMENT_INFO_KEY)
    private PaymentInfoPO paymentInfo;
    @DynamoDBAttribute(attributeName = TOTAL_AMOUNT_KEY)
    private double totalAmount;
    @DynamoDBAttribute(attributeName = NOTES_KEY)
    private String notes;
    @DynamoDBAttribute(attributeName = EMAIL_FROM_KEY)
    private String emailFrom;
}
