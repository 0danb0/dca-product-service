package com.danb.dca.product_serivce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceItemDto {
    String code;
    String description;
    int quantity;
    double unitPrice;
    double discount;
    double total;
}
