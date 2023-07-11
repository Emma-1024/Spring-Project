/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot;

import com.myspringboot.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test-mysql")
public class ApplicationTest {
    @Autowired
    private MenuService menuService;

    @Test
    public void testSelectPermsByUserId() throws InterruptedException {
        List<String> list = menuService.getPermsByUserId(2L);
        System.out.println("Test result" + list);
    }
}
