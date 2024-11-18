package com.zazhi.judger.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: docker 配置类
 */

@Configuration
@Slf4j
public class DockerConfig {

    /**
     * 配置 Docker 客户端
     *
     * @return DockerClient
     */
    @Bean
    public DockerClient dockerClient() {
        log.info("生成 Docker 客户端");
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        /* 以下是配置 Docker 客户端的示例代码，实际使用时需要根据自己的需求进行配置, 若推送镜像等操作, 则用下面的配置*/
//        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost("tcp://localhost:2375")
////                .withDockerTlsVerify(true)
////                .withDockerCertPath("/home/user/.docker")
//                .withRegistryUsername("zazhiii")
//                .withRegistryPassword("******")
//                .withRegistryEmail("lixinhuan666@gamil.com")
//                .withRegistryUrl("https://index.docker.io/v1/")
//                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }
}
