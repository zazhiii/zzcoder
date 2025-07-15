package com.zazhi.judger.docker;

import com.zazhi.judger.docker.containers.DockerContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Deprecated
public class DockerContainerPool {

    // 容器池，使用阻塞队列管理容器的空闲状态
    private final BlockingQueue<DockerContainer> queue;

    public BlockingQueue<DockerContainer> getAllContainerPool() {
        return queue;
    }

    // 初始化时，创建指定数量的容器
    public DockerContainerPool(int poolSize) {
        queue = new ArrayBlockingQueue<>(poolSize);
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
