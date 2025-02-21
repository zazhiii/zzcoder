package com.zazhi.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Duration;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: TODO
 */

//@SpringBootTest
@Slf4j
public class DockerClientTest {

    DockerClient getDockerClient() {
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


    /**
     * 检查容器状态
     */
    @Test
    void inspect() {
        DockerClient dockerClient = getDockerClient();
        InspectContainerResponse response = dockerClient.inspectContainerCmd("c49ff634ea8236fcc81cd4e7f211b7656afd7b5f5fccb75d7bb068073353da8c").exec();
        System.out.println(response.getState().getRunning());
    }
}
