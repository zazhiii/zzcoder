package com.zazhi.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.util.Set;

/**
 * @author zazhi
 * @date 2024/11/16
 * @description: TODO
 */

//@SpringBootTest
@Slf4j
public class DockerClientTest {

    private DockerClient getDockerClient() {
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

    @Test
    public void create(){
        DockerClient dockerClient = getDockerClient();

        CreateContainerResponse resp = dockerClient.createContainerCmd("sha256:6b5e78ff1e35c6a6bccca41deb9587078c74020df2ea648fdf991b7fdb6485db")
                .withTty(true)
                .exec();

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

    @Test
    void buildImage() {

//        String dockerfilePath = this.getClass().getClassLoader().getResource("Dockerfile").getPath();
        String dockerfilePath = "E:\\code_java\\zzcoder\\zzcoder-judger\\src\\main\\resources\\Dockerfile";

        DockerClient dockerClient = getDockerClient();
        String imageId = dockerClient.buildImageCmd(new File(dockerfilePath))
//                .withDockerfilePath(dockerfilePath)
                .withTags(Set.of("zzcoder-judger:1.0"))
                .exec(new BuildImageResultCallback() {
                    @Override
                    public void onNext(BuildResponseItem item) {
                        // 输出构建过程日志
                        if (item.getStream() != null) {
                            System.out.print(item.getStream().trim() + "\n");
                        }
                        super.onNext(item);
                    }
                }).awaitImageId();


        System.out.println("Built image ID: " + imageId);

    }
}
