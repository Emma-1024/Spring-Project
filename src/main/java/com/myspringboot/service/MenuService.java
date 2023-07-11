package com.myspringboot.service;

import com.myspringboot.mapper.MenuMapper;
import com.myspringboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public List<String> getPermsByUserId(long id) {
        return menuMapper.getPermsByUserId(id);
    }
}
