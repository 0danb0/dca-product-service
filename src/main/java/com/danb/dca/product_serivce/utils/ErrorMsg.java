package com.danb.dca.product_serivce.utils;

import lombok.Getter;

@Getter
public enum ErrorMsg {

    DCAPRDSRV01("DCAPRDSRV01", "Invalid fields:", ""),

    DCAPRDSRV02("DCAPRDSRV02", "Invalid fields: application-license", "Invalid or inactive key, contact support."),

    DCAPRDSRV99("DCAPRDSRV99", "Generic error", "");

    private final String code;

    private final String message;

    private final String detail;


    ErrorMsg(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}

