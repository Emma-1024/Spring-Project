/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.filter;

import com.myspringboot.service.impl.UserDetailsServiceImpl;
import com.myspringboot.utils.JwtUtil;
import com.myspringboot.utils.RedisUtil;
import com.myspringboot.vo.LoginUser;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get token
        String token = request.getHeader("token");
        if (StringUtil.isNullOrEmpty(token)) {
            // Let it go
            filterChain.doFilter(request, response);
            return;
        }

        // Parse token for getting user id
        String username;
        try {
            username = JwtUtil.extractUsername(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token is illegal");
        }

        // Check userinfo from Redis
        LoginUser redisLoginUser = redisUtil.getValue("loggedOut:" + username);
        if (Objects.isNull(redisLoginUser)) {
            // User is logging in
            redisLoginUser = redisUtil.getValue("userInfo:" + username);
            if (Objects.isNull(redisLoginUser)) {
                // Userinfo disappear after Redis crashed
                // Get userinfo from DB
                redisLoginUser = (LoginUser) userDetailsService.loadUserByUsername(username);
                if (!Objects.isNull(redisLoginUser)) {
                    // Save user info into Redis for Authentication
                    redisUtil.setValue("userInfo:" + username, redisLoginUser);
                }
            }
            // get loginUser and put it into UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(redisLoginUser, null, redisLoginUser.getAuthorities());
            // set SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // Let it go to next step
            filterChain.doFilter(request, response);
        } else {
            throw new RuntimeException("The user is not logged in.");
        }
    }
}
