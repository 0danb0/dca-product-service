package com.danb.dca.product_serivce.enums;

import lombok.Getter;

import static com.danb.dca.product_serivce.utils.ConstantStrings.*;

@Getter
public enum ConstantEnum {

    APPLICATION_NAME(APPLICATION_NAME_STRING),
    HEADER_APP_KEY_NAME(HEADER_APP_KEY_NAME_STRING),
    S3_GENERIC_ERROR_MESSAGE(S3_GENERIC_ERROR_MESSAGE_STRING);

    private final String value;

    ConstantEnum(String value){
        this.value = value;
    }
}
