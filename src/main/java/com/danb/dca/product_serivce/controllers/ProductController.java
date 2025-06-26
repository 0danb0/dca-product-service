package com.danb.dca.product_serivce.controllers;

import com.danb.dca.product_serivce.builders.GenericBuilder;
import com.danb.dca.product_serivce.models.response.GenericHealthCheckResponse;
import com.danb.dca.product_serivce.utils.DomainMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final GenericBuilder genericBuilder;

    @GetMapping("/product/health")
    public ResponseEntity<Object> productHealthCheck() {
        GenericHealthCheckResponse genericHealthCheckResponse = genericBuilder.genericHealthCheckResponse(DomainMsg.PRODUCT_SERVICE_TECHNICAL.getName(),"Product");
        return new ResponseEntity<>(genericHealthCheckResponse,HttpStatus.OK);
    }
}
