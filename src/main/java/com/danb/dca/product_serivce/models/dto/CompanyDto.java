package com.danb.dca.product_serivce.models.dto;

import lombok.Data;

@Data
public class CompanyDto {
    private String name;
    private String address;
    private String vatNumber;
    private String fiscalCode;
    private String email;
    private String phone;
}
