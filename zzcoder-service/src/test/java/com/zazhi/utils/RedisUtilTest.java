package com.zazhi.utils;

import com.zazhi.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

//@SpringBootTest
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void set() {
        redisUtil.set("AAA", "BBB", 20, TimeUnit.SECONDS);


        System.out.println(redisUtil.get("AAA"));
    }
}