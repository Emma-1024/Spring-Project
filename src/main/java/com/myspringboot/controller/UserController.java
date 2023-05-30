/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.controller;
import com.myspringboot.model.User;
import com.myspringboot.service.UserService;
import com.myspringboot.vo.Result;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user")
    public ResponseEntity<Result<User>> getUser(@RequestParam(value = "email", required = true) String email) {
        Result<User> result = new Result<>();
        if (email == null) {
            result.setMessage("email is null").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = userService.getUserByEmail(email);
        result.setData(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Result<List<User>>> getUsers() {
        // TODO PAGINATION
        List<User> userList = userService.getUsers();
        Result<List<User>> result = new Result<>(userList);
        logger.info("{}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<Result<Integer>> createUser(@RequestBody User user) {
        Integer count = userService.insert(user);
        Result<Integer> result = new Result<>(count);
        if (count == 0) {
            // TODO IMPROVE FEEDBACK
            result.setMessage("Insert failed!").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Result<Integer>> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        // TODO IMPROVE UPDATE
        var targetUser = userService.getUserById(id);
        targetUser
                .setPhoneNumber(user.getPhoneNumber())
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setPassword(user.getPassword());
        Integer count = userService.update(targetUser);
        Result<Integer> result = new Result<>(count);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Result<Integer>> deleteUser(@PathVariable("id") Long id) {
        Integer count = userService.delete(id);
        Result<Integer> result = new Result<>(count);
        if (count == 0) {
            result.setMessage("Delete ID not existed!").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
