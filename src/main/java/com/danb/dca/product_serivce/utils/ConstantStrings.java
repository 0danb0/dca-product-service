package com.danb.dca.product_serivce.utils;

public interface ConstantStrings {
    // ==============================
    // Generic constants
    // ==============================
    String APPLICATION_NAME_STRING = "dca-product-service";
    String HEADER_APP_KEY_NAME_STRING = "X-APP-KEY";
    String GENERIC_FILE_PROCESSING_ERROR_STRING = "Error during file processing";
    // ==============================
    // S3 Code and status message
    // ==============================
    String S3_GENERIC_ERROR_MESSAGE_STRING = "S3 upload file error";
    String S3_SERVICE_STATUS_CREATED_MESSAGE_STRING = "CREATED";
    String S3_SERVICE_STATUS_SUCCESS_CODE_STRING = "0";
    String S3_SERVICE_STATUS_NO_CREATION_NEEDED_MESSAGE_STRING = "NO_CREATION_NEEDED";
    String S3_SERVICE_STATUS_UPLOADED_MESSAGE_STRING = "UPLOADED";
    String S3_SERVICE_STATUS_ERROR_MESSAGE_STRING = "ERROR";
    String S3_SERVICE_STATUS_ERROR_CODE_STRING = "-1";
    String S3_SERVICE_STATUS_WAIT_MESSAGE_STRING = "WAIT";
    String S3_SERVICE_STATUS_WAIT_CODE_STRING = "";
    // ==============================
    // Database key
    // ==============================
    String PK_KEY = "pk";
    String SK_KEY = "sk";
    String CREATION_DATE_KEY = "creation_date";
    String APPLICATION_ID_KEY = "application_id";
    String INVOICE_UUID_KEY = "invoice_uuid";
    String INVOICE_NUMBER_KEY = "invoice_number";
    String INVOICE_DATE_KEY = "invoice_date";
    String NAME_KEY = "name";
    String ADDRESS_KEY = "address";
    String VAT_NUMBER_KEY = "vat_number";
    String FISCAL_CODE_KEY = "fiscal_code";
    String EMAIL_KEY = "email";
    String PHONE_KEY = "phone";
    String ISSUER_KEY = "issuer";
    String RECIPIENT_KEY = "recipient";
    String METHOD_KEY = "method";
    String IBAN_KEY = "iban";
    String ACCOUNT_HOLDER_KEY = "account_holder";
    String DUE_DATE_KEY = "due_date";
    String DUE_AMOUNT_KEY = "due_amount";
    String PAYMENT_INFO_KEY = "payment_info";
    String TOTAL_AMOUNT_KEY = "total_amount";
    String NOTES_KEY = "notes";
    String EMAIL_FROM_KEY = "email_from";
    String ITEMS_KEY = "items";
    String CODE_KEY = "code";
    String DESCRIPTION_KEY = "description";
    String QUANTITY_KEY = "quantity";
    String UNIT_PRICE_KEY = "unit_price";
    String DISCOUNT_KEY = "discount";
    String TOTAL_KEY = "total";
    String ROOT_PK = "danb";
    String APPENDIX_PK = "invoice";
}
