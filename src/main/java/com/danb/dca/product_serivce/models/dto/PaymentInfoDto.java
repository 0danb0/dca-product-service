package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

@Data
public class PaymentInfoDto {
    String method;
    String iban;
    String accountHolder;
    String dueDate;
    double dueAmount;
}
