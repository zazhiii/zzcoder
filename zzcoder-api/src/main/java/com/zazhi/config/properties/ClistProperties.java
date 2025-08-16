package com.zazhi.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author lixh
 * @since 2025/8/16 12:00
 */
@Component
@ConfigurationProperties(prefix = "clist")
@Data
public class ClistProperties {
    private String url;
    private String authorization;
    private String username;
    private String apiKey;
}
