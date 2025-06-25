package com.danb.dca.product_serivce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws.dynamodb")
public class DynamoDBProperties {
    private String region;
    private String endpoint;
    private Keys keys;

    @Getter
    @Setter
    public static class Keys {
        private String access;
        private String secret;
    }
}
