/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        Logger logger = LoggerFactory.getLogger(Application.class);

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);

        ApplicationContext context = SpringApplication.run(Application.class, args);
        Environment env = context.getEnvironment();
        String applicationName = env.getProperty("spring.application.name");
        String port = env.getProperty("server.port");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String contextPath = env.getProperty("server.servlet.context-path");
        logger.info("\n--------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n\t" +
                        "Api Docs: \thttp://localhost:{}{}/v3/api-docs\n\t" +
                        "Swagger: \thttp://localhost:{}{}/swagger-ui.html\n" +
                        "---------------------------------------",
                applicationName,
                port, contextPath,
                hostAddress, port, contextPath,
                port, contextPath,
                port, contextPath);
    }
}
