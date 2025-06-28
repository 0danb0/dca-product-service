package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

@Data
public class PaymentInfoDto {
    private String method;
    private String iban;
    private String accountHolder;
    private String dueDate;
    private double dueAmount;
}
