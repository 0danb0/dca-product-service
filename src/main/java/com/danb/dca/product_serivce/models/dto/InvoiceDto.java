package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceDto {
    String invoiceNumber;
    String invoiceDate;
    CompanyDto issuer;
    CompanyDto recipient;
    List<InvoiceItemDto> items;
    PaymentInfoDto payment;
    double totalAmount;
    String notes;
}
