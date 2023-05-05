/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.service;

import com.myspringboot.mapper.UserMapper;
import com.myspringboot.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.getAll();
    }

    public User getUserByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    public User getUserById(long id) {
        return userMapper.getById(id);
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }

    public int update(User user) {
        return userMapper.update(user);
    }

    public int delete(long id) {
        return userMapper.deleteById(id);
    }
}
