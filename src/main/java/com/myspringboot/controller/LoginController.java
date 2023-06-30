package com.myspringboot.controller;

import com.myspringboot.model.User;
import com.myspringboot.service.LoginService;
import com.myspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Result<Map>> login(@RequestBody User user){
        // login
        return loginService.login(user);
    }
}
