package com.zazhi.zzcoder.judger.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zazhi
 * @date 2025/5/2
 * @description: 判题器配置类
 */
@Component
@ConfigurationProperties(prefix = "judger")
@Data
public class JudgerProperties {
    String imageName;
    Integer containerCount;
    String containerNamePrefix;
    Integer containerMemory;
    String containerWorkDir;
}
