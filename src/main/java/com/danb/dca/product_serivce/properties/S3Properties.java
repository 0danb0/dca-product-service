package com.danb.dca.product_serivce.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {
    private String bucket;
    private String region;
    private String endpoint;
    private Keys keys;

    @Data
    public static class Keys {
        private String access;
        private String secret;
    }
}
