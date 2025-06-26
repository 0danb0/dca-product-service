package com.danb.dca.product_serivce.models.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse {
    @JsonProperty("message")
    @Size(max = 2048)
    private String message;

    @JsonProperty("detailed")
    @Size(max = 4096)
    private String detailed;

    @Size(max = 64)
    private String domain;

    @JsonProperty("timestamp")
    @Size(max = 1024)
    private String timestamp;

}
