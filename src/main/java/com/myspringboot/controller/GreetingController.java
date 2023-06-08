/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.controller;

import com.myspringboot.vo.Greeting;

import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.debug("Hello from Log4j 1");
        logger.warn("Hello from Log4j 2");
        logger.error("Hello from Log4j 3");
        logger.info("{}", 5);
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/splitStr")
    public String splitStr() {
        String input = "This is a string with, some, commas,but not, in, quote s, \"Like this, haha, hi!\", and another, \"Like this, one, too!\"";
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] items = input.split(regex);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("string", input);
        jsonObject.put("regex", regex);
        jsonObject.put("result", items);
        return jsonObject.toString();
    }
}
