/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.service.impl;

import com.myspringboot.model.User;
import com.myspringboot.service.LoginService;
import com.myspringboot.utils.JwtUtil;
import com.myspringboot.utils.RedisUtil;
import com.myspringboot.vo.LoginUser;
import com.myspringboot.vo.Result;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResponseEntity<Result<Map>> login(User user) {
        Result<Map> result = new Result<>();
        Map<String, String> map = new HashMap<>();

        // Use authenticate function provided by AuthenticationManager to identify a user
        // Through calling loadUerByUsername function
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());

        // Give hint if certification is successful or failed
        try {
            // TODO Need to get authorization info and encapsulated into Authentication
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // Use username to generate a jwt which saved into ResponseResult and be returned, if certification passed
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String username = loginUser.getUser().getName();
            String jwt = JwtUtil.generateToken(username);
            map.put("token", jwt);
            result.setMessage("Welcome! You have successfully logged in.")
                    .setSuccess(true)
                    .setData(map);

            // 产生token之后，看Redis有没有（{emmaWorkingToken: "xxxx"})，
            // -- 没有的话在Redis中创建一对
            // -- 有的话check是否有黑名单
            // -- 没有黑名单，先创建黑名单
            // -- 有黑名单以及有了黑名单，
            // -- 直接将之前的token加入黑名单（{blockedTokens:[token1, token2]}),然后将新token更新到{emmaWorkingToken: "xxxx"})
            String redisWorkingTokenKey = username + "#WorkingToken";
            String workingToken = redisUtil.getValue(redisWorkingTokenKey, String.class);
            if (Objects.isNull(workingToken)) {
                redisUtil.setValue(redisWorkingTokenKey, jwt);
            } else {
                redisUtil.processBlockedToken(username, workingToken);
                redisUtil.setValue(redisWorkingTokenKey, jwt);
            }

            // Considering when I implement a whitelist mechanism to store login token,
            // Need another key to temporarily save userinfo for authentication in JwtAuthenticationTokenFilter
            redisUtil.setValue(username + "#userInfo", loginUser);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AuthenticationException e) {
            result.setMessage("Authentication fails").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Result<String>> logout() {
        // Get username from SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUser().getName();

        // Get user info from Redis
        redisUtil.deleteKeyFromRedis(username + "#userInfo");

        Result<String> result = new Result<>(username);
        result.setMessage("Logged out").setSuccess(true);

        // 将当前token加入黑名单防止下次继续登录，并删除keyValue Object
        String redisWorkingTokenKey = username + "#WorkingToken";
        String workingToken = redisUtil.getValue(redisWorkingTokenKey, String.class);
        redisUtil.processBlockedToken(username, workingToken);
        redisUtil.deleteKeyFromRedis(redisWorkingTokenKey);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
