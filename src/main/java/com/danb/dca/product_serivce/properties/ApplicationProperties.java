package com.danb.dca.product_serivce.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "dca.application")
public class ApplicationProperties {
    private Map<String, String> licensed;
}
