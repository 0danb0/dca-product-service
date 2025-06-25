package com.danb.dca.product_serivce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.application")
public class ApplicationProperties {
    private String name;
    private Map<String, String> licensed;
}
