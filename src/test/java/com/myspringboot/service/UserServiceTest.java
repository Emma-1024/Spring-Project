package com.myspringboot.service;

import com.myspringboot.ApplicationTest;
import com.myspringboot.mapper.UserMapper;
import com.myspringboot.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserServiceTest extends ApplicationTest {

    private User user;
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @AfterEach
    public void cleanUp() {
        userMapper.deleteAll();
    }

    @Test
    public void testInsertAndSelect() {
        User findedUser = initializeUser();
        // Check that the data just inserted exits
        assertThat(findedUser.getName()).isEqualTo("test");
        assertThat(findedUser.getPassword()).isEqualTo("123");
        assertThat(findedUser.getEmail()).isEqualTo("test@mail.com");
        assertThat(findedUser.getPhoneNumber()).isEqualTo("123456789");

        // Get all users
        List<User> userList = userService.getUsers();
        assertThat(userList.get(0).getName()).isEqualTo("test");
        assertThat(userList.get(0).getPassword()).isEqualTo("123");
        assertThat(userList.get(0).getEmail()).isEqualTo("test@mail.com");
        assertThat(userList.get(0).getPhoneNumber()).isEqualTo("123456789");

        // Delete action
        long id = findedUser.getId();
        int count = userService.delete(id);
        assertThat(count).isEqualTo(1);
        // Check that the data have been deleted
        findedUser = userService.getUserById(id);
        assertThat(findedUser).isNull();
    }

    @Test
    public void testUpdate() {
        User findedUser = initializeUser();
        User originalUser = userService.getUserById(findedUser.getId());
        user.setName("testUpdate").setPassword("333").setEmail("testUpdate@mail.com").setPhoneNumber("000000000").setId(originalUser.getId());
        int count = userService.update(user);
        assertThat(count).isEqualTo(1);

        User updatedUser = selectUserByEmail("testUpdate@mail.com");
        updatedUser = userService.getUserById(updatedUser.getId());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("testUpdate");
        assertThat(updatedUser.getPassword()).isEqualTo("333");
        assertThat(updatedUser.getEmail()).isEqualTo("testUpdate@mail.com");
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("000000000");
        // reset
        userService.update(originalUser);
        assertThat(updatedUser).isNotNull();
    }

    @Test
    public void testDelete() {
        User toBeDeletedUser = initializeUser();
        //Delete by id
        int count = userService.delete(toBeDeletedUser.getId());
        assertThat(count).isEqualTo(1);
        // Check if the data exists
        toBeDeletedUser = userService.getUserById(toBeDeletedUser.getId());
        assertThat(toBeDeletedUser).isNull();
    }

    private User initializeUser() {
        userMapper.deleteAll();
        // Insert
        user.setName("test").setPassword("123").setEmail("test@mail.com").setPhoneNumber("123456789");
        User findedUser = userService.getUserByEmail("test@mail.com");
        assertThat(findedUser).isNull();
        userService.insert(user);

        findedUser = selectUserByEmail("test@mail.com");
        return findedUser;
    }
    private User selectUserByEmail(String email){
        // SelectByEmail
        User findedUser = userService.getUserByEmail(email);
        assertThat(findedUser).isNotNull();
        return findedUser;
    }
}
