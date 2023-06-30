package com.myspringboot.service.impl;

import com.myspringboot.mapper.UserMapper;
import com.myspringboot.model.User;
import com.myspringboot.utils.JwtUtil;
import com.myspringboot.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Query user information
        User user = userMapper.getByName(username);
        // No user is queried, throw exception
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user name or password is incorrect");
        }
        // Test if jwt can be used
//        JwtUtil jwti = new JwtUtil();
//        LoginUser loginUser = new LoginUser(user);
//        String jwt =  jwti.generateToken(loginUser);
//        System.out.println("###"+ jwti.validateToken(jwt, loginUser));
//        System.out.println("###"+jwt);
        // TODO Query current user's permission information

        // Encapsulate the data into UserDetails and return
        return new LoginUser(user);
    }
}
