package com.danb.dca.product_serivce.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductException extends Exception {
    @NotBlank(message = "code is mandatory")
    @JsonProperty("code")
    @Size(min = 1, max = 16)
    private String code;

    @NotBlank(message = "message is mandatory")
    @JsonProperty("message")
    @Size(max = 2048)
    private String message;

    @JsonProperty("domain")
    @Size(max = 64)
    private String domain;

    @JsonProperty("detailed")
    @Size(max = 4096)
    private String detailed;

}
