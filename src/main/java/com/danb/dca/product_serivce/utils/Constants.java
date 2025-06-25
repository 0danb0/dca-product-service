package com.danb.dca.product_serivce.utils;

import lombok.Getter;

@Getter
public enum Constants {

    APPLICATION_NAME("dca-product-service"),

    HEADER_APP_KEY_NAME("X-APP-KEY");

    private final String value;

    Constants(String value){
        this.value = value;
    }
}
