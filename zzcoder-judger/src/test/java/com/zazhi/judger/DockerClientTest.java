package com.zazhi.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: TODO
 */


public class DockerClientTest {

    private static final Logger log = LoggerFactory.getLogger(DockerClientTest.class);

    private DockerClient dockerClient;

    @Test
    void run() throws IOException {
        main();
        // 代码所在的绝对路径
        String absolutePath = "C:/tmp/judgeTask_23";

        try {
            // 创建并启动容器
            CreateContainerResponse container = dockerClient.createContainerCmd("openjdk:latest")
                    .withHostConfig(
                            HostConfig.newHostConfig()
                                    .withBinds(new Bind(absolutePath, new Volume("/app"))) // 挂载路径
                    )
                    .withWorkingDir("/app") // 设置工作目录
    //                .withCmd("javac", "Main.java") // 编译的命令
                    .withCmd(
                            "/bin/sh", "-c",
                            "java Main < /app/input.txt > /app/output.txt") // 运行的命令
    //                .withTty(true) // 可选：启用 TTY 模式
                    .exec();

            dockerClient.startContainerCmd(container.getId()).exec();
            // 等待容器运行完成
            int exitCode = dockerClient.waitContainerCmd(container.getId())
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode(); // 等待容器状态码（完成后返回退出码）
            System.out.println("Container exited with code: " + exitCode);

            // 删除容器（模拟 --rm）
            dockerClient.removeContainerCmd(container.getId()).exec();
        }catch (Exception e) {
            log.error("运行容器失败", e);
            e.printStackTrace();
        }finally {
            dockerClient.close();
        }

    }

    @Test
    void main() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

//        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost("tcp://localhost:2375")
////                .withDockerTlsVerify(true)
////                .withDockerCertPath("/home/user/.docker")
//                .withRegistryUsername("zazhiii")
//                .withRegistryPassword("LXH030821")
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

        this.dockerClient = DockerClientImpl.getInstance(config, httpClient);
    }
}
