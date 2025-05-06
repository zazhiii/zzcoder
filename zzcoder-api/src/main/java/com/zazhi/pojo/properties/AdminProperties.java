package com.zazhi.pojo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zazhi
 * @date 2025/5/2
 * @description: 管理员账号配置
 */
@Component
@ConfigurationProperties(prefix = "zzcoder.admin")
@Data
public class AdminProperties {
    private String roleName;
    private String username;
    private String password;
    private String email;
}
