/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public void setValue(final String key, Object src) {
        redisTemplate.opsForValue().set(key, gson.newBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJson(src));
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    public <T> T getValue(final String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        return gson.newBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().fromJson(json, clazz);
    }

    public <T> List<T> getValueByTypeToken(final String key, Class<T> elementType) {
        Type type = TypeToken.getParameterized(List.class, elementType).getType();
        return gson.newBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().fromJson(redisTemplate.opsForValue().get(key), type);
    }

    public void deleteKeyFromRedis(String key) {
        redisTemplate.delete(key);
    }

    public void processBlockedToken(String username, String workingToken) {
        String redisBlockedTokensKey = username + "BlockedTokens";
        List<String> blockedTokenList = getValueByTypeToken(redisBlockedTokensKey, String.class);
        if (Objects.isNull(blockedTokenList)) {
            List<String> tempBlockedTokenList = new ArrayList<>();
            tempBlockedTokenList.add(workingToken);
            setValue(redisBlockedTokensKey, tempBlockedTokenList);
        } else {
            blockedTokenList.add(workingToken);
            setValue(redisBlockedTokensKey, blockedTokenList);
        }
    }
}
