package com.danb.dca.product_serivce.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZapierData {
    @JsonProperty("application-license")
    private String applicationLicense;
    @JsonProperty("email-from")
    private String emailFrom;
}
