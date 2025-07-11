package com.zazhi.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zzcoder.verify.code")
@Data
public class VerifyCodeProperties {
    private long expire;
    private int length;
    private long interval;
}