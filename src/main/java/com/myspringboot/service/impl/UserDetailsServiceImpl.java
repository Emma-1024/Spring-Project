/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.service.impl;

import com.myspringboot.mapper.UserMapper;
import com.myspringboot.model.User;
import com.myspringboot.vo.LoginUser;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Query user information
        User user = userMapper.getByName(username);
        // No user is queried, throw exception
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user name or password is incorrect");
        }
        // TODO Query current user's Authorization information

        // Encapsulate the data into UserDetails and return
        return new LoginUser(user);
    }
}
