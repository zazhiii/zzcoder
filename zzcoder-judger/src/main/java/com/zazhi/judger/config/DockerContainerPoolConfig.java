package com.zazhi.judger.config;

import com.github.dockerjava.api.DockerClient;
import com.zazhi.judger.docker.DockerContainerPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zazhi
 * @date 2025/2/21 
 * @description: Docker容器池配置
 */
@Configuration
public class DockerContainerPoolConfig {

    public static final int POOL_SIZE = 17;

    @Bean
    public DockerContainerPool dockerContainerPool(DockerClient dockerClient) {
        return new DockerContainerPool(POOL_SIZE, dockerClient);
    }
}
