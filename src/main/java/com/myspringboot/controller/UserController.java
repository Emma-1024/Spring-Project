/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.controller;

import com.myspringboot.model.User;
import com.myspringboot.service.UserService;
import com.myspringboot.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Retrieve a user by email", description = "Get a user object by specifying its email.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(
                    responseCode = "404",
                    description = "The user with given email was not found.",
                    content = {@Content(schema = @Schema())})
    })
    @Parameters({@Parameter(name = "email", description = "Search a user by email")})
    @GetMapping("/user")
    public ResponseEntity<Result<User>> getUser(@RequestParam(value = "email", required = true) String email) {
        Result<User> result = new Result<>();
        if (email == null) {
            result.setMessage("email is null").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = userService.getUserByEmail(email);
        result.setData(user);
        if (user == null) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all users", description = "Get users list.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
    })
    @GetMapping("/users")
    public ResponseEntity<Result<List<User>>> getUsers() {
        // TODO PAGINATION
        List<User> userList = userService.getUsers();
        Result<List<User>> result = new Result<>(userList);
        logger.info("{}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Insert a user", description = "Insert a user object.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
    })
    @PostMapping(value = {"/user", "/signin"})
    public ResponseEntity<Result<Integer>> createUser(@RequestBody User user) {
        Integer count = 0;
        Result<Integer> result = new Result<>(count);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            count = userService.insert(user);
            result.setData(count);
        } catch (DuplicateKeyException e) {
            result.setMessage("Duplicated key!").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // TODO IMPROVE FEEDBACK
        if (count == 0) {
            result.setMessage("Insert failed!").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Update a user by id", description = "Update a user object by specifying its id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
    })
    @Parameters({@Parameter(name = "id", description = "Update a user by id")})
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
        if (count == 0) {
            result.setMessage("The update failed! ").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Delete a user by id", description = "Delete a user object by specifying its id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
    })
    @Parameters({@Parameter(name = "id", description = "Delete a user by id")})
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Result<Integer>> deleteUser(@PathVariable("id") Long id) {
        Integer count = userService.delete(id);
        Result<Integer> result = new Result<>(count);
        if (count == 0) {
            result.setMessage("The id to be deleted doesn't exist").setSuccess(false);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
