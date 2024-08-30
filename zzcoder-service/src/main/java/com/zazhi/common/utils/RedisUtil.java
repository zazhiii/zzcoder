package com.zazhi.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: redis相关操作工具类
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /* ================ String操作 ================== */

    /**
     * 设置指定 key 的值
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    单位
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
