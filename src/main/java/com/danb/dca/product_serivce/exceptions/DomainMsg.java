package com.danb.dca.product_serivce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainMsg {

    PRODUCT_SERVICE_TECHNICAL("DCAProductServiceTechnicals"),
    INVOICE_SERVICE_TECHNICAL("DCAINVOICEServiceTechnicals"),
    MICROSERVICE_FUNCTIONAL("MicroServiceFunctional");

    private final String name;
}

