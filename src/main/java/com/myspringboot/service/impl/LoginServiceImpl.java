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
import java.util.HashMap;
import java.util.Map;
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
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
        Authentication authentication = null;

        // Give hint if certification failed
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            result.setMessage("Authentication fails").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }

        // Use userId to generate a jwt which saved into ResponseResult and be returned, if certification passed
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.generateToken(userId);
        map.put("token", jwt);
        result.setMessage("Welcome! You have successfully logged in.")
                .setSuccess(true)
                .setData(map);

        // TODO Save complete information into Redis and userId is the key
        redisUtil.setValue("login:" + userId, loginUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Result<Long>> logout() {
        // Get user id from SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userId = loginUser.getUser().getId();

        // Delete value from Redis
        redisUtil.deleteKeyFromRedis("login:" + userId);
        Result<Long> result = new Result<>(userId);
        result.setMessage("Logged out").setSuccess(true);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
