package com.myspringboot.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.myspringboot.dto.Greeting;
import com.myspringboot.model.User;
import com.myspringboot.mapper.UserMapper;
import com.myspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    UserService userService;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/createUser")
    public User createUser() {
        // insert one user data
        User user = new User();
        user.setName("Emily");
        user.setEmail("emily@gmail.com");
        user.setPassword("2222");
        user.setPhone_number("13456783456");
        userService.insert(user);
        return user;
    }

    @GetMapping("/user")
    public User getUserById(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name) {
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

