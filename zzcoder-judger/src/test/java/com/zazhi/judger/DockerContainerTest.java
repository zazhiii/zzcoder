package com.zazhi.judger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.zazhi.judger.dockerpool.containers.CodeExecContainer;
import com.zazhi.judger.dockerpool.pojo.CmdExecResult;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author lixh
 * @since 2025/7/19 23:01
 */
//@SpringBootTest
//@RequiredArgsConstructor
public class DockerContainerTest {

    private DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

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
    void testCompileAndRunJavaCode() throws InterruptedException {

        DockerClient dockerClient = getDockerClient();

        String cid = "8469bd7c0280e5cad8a688e32ed94a0fadeaec502875afa8322fcbbb9dbef14f";

        CodeExecContainer container = new CodeExecContainer(dockerClient, cid, "", "G:\\zzcoder-judger-work\\0c9684f1-69c5-4acf-8832-128c7cd84d65");

        String[] compileCmd = {"javac", "Main.java"};
        CmdExecResult res = container.execCmd(compileCmd, 10, TimeUnit.SECONDS);
        System.out.println("编译结果: " + res.getStdout());
        System.out.println("编译错误: " + res.getStderr());

        String[] runCmd = {"java", "Main"};
        CmdExecResult runRes = container.execCmd(runCmd, "1 2\n", 10, TimeUnit.SECONDS);
        System.out.println("运行结果: " + runRes.getStdout());
        System.out.println("运行错误: " + runRes.getStderr());
    }

}
