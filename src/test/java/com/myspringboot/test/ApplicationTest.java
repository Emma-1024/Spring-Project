package com.myspringboot.test;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import com.myspringboot.mapper.UserMapper;

@MapperScan(basePackages = { "com.example.mapper" }, annotationClass = Mapper.class)
public class ApplicationTest {

    static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);

    @Autowired
    UserMapper fooMapper;

    @Test
    @Transactional(readOnly = true)
    @Rollback(false)
    public void test () {
//        LOGGER.info("result={}", this.fooMapper.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
