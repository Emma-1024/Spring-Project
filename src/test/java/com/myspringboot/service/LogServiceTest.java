/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.service;

import com.myspringboot.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LogServiceTest extends ApplicationTest {

    @Autowired
    LogService logService;

    @Test
    public void testBeforeLog() {
        logService.logCatching();
    }
}
