/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.utils;

import com.google.gson.Gson;
import com.myspringboot.vo.LoginUser;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    Gson gson;

    public void setValue(final String key, LoginUser loginUser) {
        redisTemplate.opsForValue().set(key, gson.toJson(loginUser));
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
    }

    public LoginUser getValue(final String key) {
        return gson.fromJson(redisTemplate.opsForValue().get(key), LoginUser.class);
    }

    public void deleteKeyFromRedis(String key) {
        redisTemplate.delete(key);
    }
}
