package com.zazhi.judger.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.zazhi.judger.common.properties.JudgerProperties;
import com.zazhi.judger.docker.DockerContainer;
import com.zazhi.judger.docker.DockerContainerPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author zazhi
 * @date 2025/2/21 
 * @description: Docker容器池配置
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DockerContainerPoolConfig {

    private final JudgerProperties judgerProp;

    @Bean
    public DockerContainerPool dockerContainerPool(DockerClient dockerClient) {
        int poolSize = judgerProp.getContainerCount(); // 容器池大小
        DockerContainerPool pool = new DockerContainerPool(poolSize);
        for(int i = 0; i < poolSize; i++) {
            String containerName = judgerProp.getContainerNamePrefix() + String.format("%02d", i);
            // 创建工作目录用于存放用户提交的代码和文件
            String workDirPath = judgerProp.getContainerWorkDir() + containerName;
            File workDir = new File(workDirPath);
            workDir.mkdir();
//            if(!workDir.mkdir()){
//                log.error("创建工作目录失败: {}", workDirPath);
//                continue;
//            }
            // 宿主机的临时目录绝对路径
            String workAbsPath = workDir.getAbsolutePath();
            String image = judgerProp.getImageName();
            int memoryLimit = judgerProp.getContainerMemory();
            CreateContainerResponse createRes = dockerClient.createContainerCmd(image)
                    .withHostConfig(
                            HostConfig.newHostConfig()
                                    .withBinds(new Bind(workAbsPath, new Volume("/app"))) // 挂载路径注意要挂载绝对路径，否则会有点问题
                                    .withMemory(memoryLimit * 1024 * 1024L) // 设置最大内存限制
                                    .withMemorySwap(memoryLimit * 1024 * 1024L) // 禁止交换分区 (swap)
                    )
                    .withName(containerName)
                    .withWorkingDir("/app") // 设置工作目录
                    .exec();

            log.info("创建容器：ID: {}", createRes.getId());

            DockerContainer container = new DockerContainer(dockerClient, createRes.getId(), containerName, workDirPath);
            pool.releaseContainer(container); // 将容器放入池中
        }
        return pool;
    }
}
