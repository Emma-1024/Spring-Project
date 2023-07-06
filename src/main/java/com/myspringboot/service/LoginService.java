/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.service;

import com.myspringboot.model.User;
import com.myspringboot.vo.Result;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<Result<Map>> login(User user);

    ResponseEntity<Result<String>> logout();
}
