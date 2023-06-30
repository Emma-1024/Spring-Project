package com.myspringboot.service.impl;

import com.myspringboot.model.User;
import com.myspringboot.service.LoginService;
import com.myspringboot.utils.JwtUtil;
import com.myspringboot.vo.LoginUser;
import com.myspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Result<Map>> login(User user) {
        Result<Map> result = new Result<>();
        Map<String, String> map = new HashMap<>();

        // Use authenticate function provided by AuthenticationManager to identify a user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
        Authentication authentication = null;

        // Give hint if certification failed
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            result.setMessage("Authentication fails").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }

        // Use user to generate a jwt which saved into ResponseResult and be returned, if certification passed
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String jwt = JwtUtil.generateToken(loginUser);
        map.put("token", jwt);
        result.setMessage("Welcome! You have successfully logged in.").setSuccess(true).setData(map);
        // Save complete information into Redis and userId is the key

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
