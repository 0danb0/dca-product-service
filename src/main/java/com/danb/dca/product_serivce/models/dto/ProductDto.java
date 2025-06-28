package com.danb.dca.product_serivce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private String code;
    private String description;
    private int quantity;
    private double unitPrice;
    private double discount;
    private double total;
}
