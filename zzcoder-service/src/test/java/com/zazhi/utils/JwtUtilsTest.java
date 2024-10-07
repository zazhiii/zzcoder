package com.zazhi.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zazhi
 * @date 2024/10/7
 * @description: 测试JwtUtils
 */
@SpringBootTest
public class JwtUtilsTest {
//    @Autowired
//    JwtUtil jwtUtil;

    @Test
    public void test(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 10L);
        map.put("username", "zazhi");
        String token = JwtUtil.genToken(map);

        Map<String, Object> map1 = JwtUtil.parseToken(token);
        System.out.println(map1.get("id").getClass().getTypeName());
    }
}
