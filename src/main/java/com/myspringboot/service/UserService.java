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

    public List<User> getUsers(String name) {
        if(name==null) {
            return userMapper.getAll();
        }
        return Arrays.asList(userMapper.getByName(name));
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }

    // TODO ...
}
