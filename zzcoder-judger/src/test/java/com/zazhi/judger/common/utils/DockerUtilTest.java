package com.zazhi.judger.common.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DockerUtilTest {

    @Autowired
    private DockerUtil dockerUtil;

//    @Test // tested
    void createContainer() {
        dockerUtil.createContainer("C:/tmp/judgeTask_23", 512, "java_judger", "createContainerTest");
    }
}