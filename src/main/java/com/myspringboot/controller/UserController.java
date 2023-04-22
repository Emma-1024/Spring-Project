package com.myspringboot.controller;

import com.myspringboot.model.User;
import com.myspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        userService.insert(user);
        return user;
    }


    @GetMapping("/user")
    public User getUser(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name) {
        if (id != null) {
            return userService.getUserById(id);
        } else if (name != null) {
            return userService.getUserByName(name);
        } else {
            return null;
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        userService.update(id, "bob", "test090909");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
