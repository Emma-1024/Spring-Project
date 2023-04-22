package com.myspringboot.service;

import com.myspringboot.mapper.UserMapper;
import com.myspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.getAll();
    }

    public User getUserByName(String name) {
        return userMapper.getByName(name);
    }

    public User getUserById(long id) {
        return userMapper.getById(id);
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }

    public int update(long id, String name, String password) {
        return userMapper.updateById(id, name, password);
    }

    public int delete(long id) {
        return userMapper.deleteById(id);
    }
}
