/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myspringboot.vo.LoginUser;

import java.lang.reflect.Modifier;
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
        redisTemplate.opsForValue().set(key, gson.newBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJson(loginUser));
//        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    public LoginUser getValue(final String key) {
        return gson.newBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().fromJson(redisTemplate.opsForValue().get(key), LoginUser.class);
    }

    public void deleteKeyFromRedis(String key) {
        redisTemplate.delete(key);
    }
}
