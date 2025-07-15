package com.zazhi.judger.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lixinhuan
 * @date 2025/7/14
 * @description: DockerPoolExecutorProperties 类用于配置 Docker 池执行器的属性
 */
@Component
@ConfigurationProperties(prefix = "z-judger.docker.pool")
@Data
public class ContainerPoolExecutorProperties {

    private int maxPoolSize;

    private long keepStartTime;

    private String imageName;

    private String containerWorkingDir;

    private String hostWorkingDir;

    private int memoryLimitMb;

}
