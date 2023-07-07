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
import java.util.Objects;
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

            // Check information from Redis and username is the key
            LoginUser redisLoginUser = redisUtil.getValue("loggedOut:" + username);
            if (!Objects.isNull(redisLoginUser)) {
                redisUtil.deleteKeyFromRedis("loggedOut:" + username);
            }
            // Considering when I implement a whitelist mechanism to store login token,
            // Need another key to temporarily save userinfo for authentication in JwtAuthenticationTokenFilter
            redisUtil.setValue("userInfo:" + username, loginUser);
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
        redisUtil.setValue("loggedOut:" + username, loginUser);
        redisUtil.deleteKeyFromRedis("userInfo:" + username);

        Result<String> result = new Result<>(username);
        result.setMessage("Logged out").setSuccess(true);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
