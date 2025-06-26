package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

@Data
public class CompanyDto {
    String name;
    String address;
    String vatNumber;
    String fiscalCode;
    String email;
    String phone;
}
