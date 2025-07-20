package com.zazhi.judger.config;

import com.github.dockerjava.api.DockerClient;
import com.zazhi.judger.config.properties.ContainerPoolExecutorProperties;
import com.zazhi.judger.dockerpool.ContainerPoolExecutor;
import com.zazhi.judger.dockerpool.containers.CodeExecContainer;
import com.zazhi.judger.dockerpool.factorys.CodeExecContainerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lixinhuan
 * @date 2025/7/14
 * @description: ContainerPoolExecutorConfig 类用于配置容器池执行器的属性
 */
@Configuration
@RequiredArgsConstructor
public class ContainerPoolExecutorConfig {

    private final ContainerPoolExecutorProperties prop;

    private final DockerClient dockerClient;

    @Bean
    public ContainerPoolExecutor<CodeExecContainer> containerPoolExecutor() {
        return new ContainerPoolExecutor<>(
                prop.getMaxPoolSize(),
                prop.getKeepStartTime(),
                TimeUnit.MINUTES,
                new CodeExecContainerFactory(
                        dockerClient,
                        prop.getHostWorkingDir(),
                        prop.getContainerWorkingDir(),
                        prop.getMemoryLimitMb(),
                        prop.getImageName()
                )
        );
    }
}
