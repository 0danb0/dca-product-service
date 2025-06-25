package com.danb.dca.product_serivce.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMsg {

    DCAPRDSRV01("DCAPRDSRV01", "Invalid fields:"),

    DCAPRDSRV99("DCAPRDSRV99", "Generic error");

    private final String code;

    private final String message;


    ErrorMsg(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

