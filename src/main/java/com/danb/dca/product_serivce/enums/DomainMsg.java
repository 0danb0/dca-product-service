package com.danb.dca.product_serivce.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainMsg {

    PRODUCT_SERVICE_TECHNICAL("DCA-Product-Service-Technicals"),
    INVOICE_SERVICE_TECHNICAL("DCA-Invoice-Service-Technicals"),
    S3_SERVICE_TECHNICAL("DCA-S3-Service-Technicals"),
    API_SERVICE_TECHNICAL("DCA-API-Service-Technicals"),
    MICROSERVICE_FUNCTIONAL("MicroServiceFunctional");

    private final String name;
}

