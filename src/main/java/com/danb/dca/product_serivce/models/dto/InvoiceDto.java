package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDto {
    private String invoiceNumber;
    private String invoiceDate;
    private CompanyDto issuer;
    private CompanyDto recipient;
    private List<ProductDto> items;
    private PaymentInfoDto paymentInfo;
    private double totalAmount;
    private String notes;
}
