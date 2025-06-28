package com.danb.dca.product_serivce.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.danb.dca.product_serivce.utils.ConstantStrings.*;

@Getter
@AllArgsConstructor
public enum S3ServiceStatus {

    CREATED(S3_SERVICE_STATUS_SUCCESS_CODE_STRING,
            S3_SERVICE_STATUS_CREATED_MESSAGE_STRING),

    NO_CREATION_NEEDED(S3_SERVICE_STATUS_SUCCESS_CODE_STRING,
            S3_SERVICE_STATUS_NO_CREATION_NEEDED_MESSAGE_STRING),

    UPLOADED(S3_SERVICE_STATUS_SUCCESS_CODE_STRING,
            S3_SERVICE_STATUS_UPLOADED_MESSAGE_STRING),

    ERROR(S3_SERVICE_STATUS_ERROR_CODE_STRING,
            S3_SERVICE_STATUS_ERROR_MESSAGE_STRING),

    WAIT(S3_SERVICE_STATUS_WAIT_CODE_STRING,
            S3_SERVICE_STATUS_WAIT_MESSAGE_STRING);

    private final String code;
    private final String message;
}
