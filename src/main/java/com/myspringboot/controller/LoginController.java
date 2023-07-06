/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.controller;

import com.myspringboot.model.User;
import com.myspringboot.service.LoginService;
import com.myspringboot.vo.Result;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Result<Map>> login(@RequestBody User user) {
        // login
        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public ResponseEntity<Result<String>> logout() {
        return loginService.logout();
    }
}
