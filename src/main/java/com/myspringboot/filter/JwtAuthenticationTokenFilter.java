package com.myspringboot.filter;

import com.myspringboot.utils.JwtUtil;
import com.myspringboot.utils.RedisUtil;
import com.myspringboot.vo.LoginUser;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get token
        String token = request.getHeader("token");
        if (StringUtil.isNullOrEmpty(token)) {
            // Let it go
            filterChain.doFilter(request, response);
            return;
        }

        // Parse token for getting user id
        String userId;
        try {
            userId = JwtUtil.extractUserId(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token is illegal");
        }

        // Get user info from Redis
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisUtil.getValue(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("The user is not logged in.");
        }
        // Save SecurityContextHolder
        // TODO Need to get authorization info and encapsulated into Authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // Let it go to next step
        filterChain.doFilter(request, response);
    }
}
