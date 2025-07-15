package com.zazhi.judger.common.utils;

import com.zazhi.judger.docker.containers.DockerContainer;
import com.zazhi.judger.docker.DockerContainerPool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zazhi
 * @date 2025/2/21 
 * @description: Docker容器池
 */
@SpringBootTest
public class DockerPoolTest {

    @Autowired
    DockerContainerPool dockerContainerPool;

    @Test
    public void test() throws InterruptedException {
        System.out.println(dockerContainerPool.getAllContainerPool().size());

        DockerContainer container1 = dockerContainerPool.acquireContainer();
        DockerContainer container2 = dockerContainerPool.acquireContainer();
        System.out.println(container1);
        System.out.println(container2);

        System.out.println(dockerContainerPool.getAllContainerPool().size());

        dockerContainerPool.releaseContainer(container1);
        dockerContainerPool.releaseContainer(container2);

        System.out.println(dockerContainerPool.getAllContainerPool().size());
    }
}
