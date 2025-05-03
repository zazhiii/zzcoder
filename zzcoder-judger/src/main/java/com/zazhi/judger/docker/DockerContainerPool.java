package com.zazhi.judger.docker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class DockerContainerPool {

    // 容器池，使用阻塞队列管理容器的空闲状态
    private final BlockingQueue<DockerContainer> queue;

    public BlockingQueue<DockerContainer> getAllContainerPool() {
        return queue;
    }

    // 初始化时，创建指定数量的容器
    public DockerContainerPool(int poolSize) {
        queue = new ArrayBlockingQueue<>(poolSize);

//        // 初始化容器池，创建多个 Docker 容器
//        for (int i = 1; i <= poolSize; i++) {
//            String containerName = "container_" + String.format("%02d", i);
//            // 创建工作目录用于存放用户提交的代码和文件
//            String workingDir = "/zzcoder/judge_containers/" + containerName;
//            File workingDirectory = new File(workingDir);
//            workingDirectory.mkdir();
//            // 宿主机的临时目录绝对路径
//            String workingDirAbsPath = workingDirectory.getAbsolutePath();
//            String image = "java_judger";
//            int memoryLimit = 256;
//
//            CreateContainerResponse createRes = dockerClient.createContainerCmd(image)
//                    .withHostConfig(
//                            HostConfig.newHostConfig()
//                                    .withBinds(new Bind(workingDirAbsPath, new Volume("/app"))) // 挂载路径注意要挂载绝对路径，否则会有点问题
//                                    .withMemory(memoryLimit * 1024 * 1024L) // 设置最大内存限制
//                                    .withMemorySwap(memoryLimit * 1024 * 1024L) // 禁止交换分区 (swap)
//                    )
//                    .withName(containerName)
//                    .withWorkingDir("/app") // 设置工作目录
//                    .exec();
//
//            log.info("创建容器：{}", createRes);
//
//            DockerContainer container = new DockerContainer(dockerClient, createRes.getId(), containerName, workingDir);
//            queue.add(container);
//        }
    }

    // 从池中获取一个空闲的容器
    public DockerContainer acquireContainer() throws InterruptedException {
        // 如果没有空闲容器，会阻塞直到有容器可用
        DockerContainer dockerContainer = queue.take();
        if(!dockerContainer.isRunning()){
            dockerContainer.start();
        }
        return dockerContainer;
    }

    // 将容器放回池中
    public void releaseContainer(DockerContainer container) {
        queue.add(container);
    }
}
