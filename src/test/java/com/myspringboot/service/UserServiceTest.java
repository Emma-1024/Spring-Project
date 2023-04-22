package com.myspringboot.service;

import com.myspringboot.ApplicationTest;
import com.myspringboot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest extends ApplicationTest {

    @Autowired
    UserService userService;

    @Test
    public void testInsert(){
        var user = new User();
        user.setName("test").setPassword("123").setEmail("test@mail.com").setPhoneNumber("123456789");
        var temp = userService.getUserByEmail("test@mail.com");
        assertThat(temp).isNull();
        userService.insert(user);
        temp = userService.getUserByEmail("test@mail.com");
        assertThat(temp).isNotNull();
        assertThat(temp.getName()).isEqualTo("test");
        assertThat(temp.getPassword()).isEqualTo("123");
        assertThat(temp.getEmail()).isEqualTo("test@mail.com");
        assertThat(temp.getPhoneNumber()).isEqualTo("123456789");
    }
}
