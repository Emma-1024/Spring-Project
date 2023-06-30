package com.myspringboot.service;

import com.myspringboot.model.User;
import com.myspringboot.vo.Result;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface LoginService {

    ResponseEntity<Result<Map>> login(User user);
}
