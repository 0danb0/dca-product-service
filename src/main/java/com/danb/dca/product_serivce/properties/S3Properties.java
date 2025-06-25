package com.danb.dca.product_serivce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {
    private String bucket;
    private String region;
    private Keys keys;

    @Getter
    @Setter
    private static class Keys {
        private String access;
        private String secret;
    }
}
