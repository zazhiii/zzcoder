package com.zazhi.zzcoder.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zzcoder.email.code")
@Data
public class EmailCodeProperties {
    private long expire;
    private int length;
    private long interval;
}