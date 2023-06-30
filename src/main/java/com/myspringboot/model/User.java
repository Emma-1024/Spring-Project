/*
 * Copyright (C)2023, emma Wu
 * All rights reserved.
 */
package com.myspringboot.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Model Information")
public class User {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "User Id", example = "1")
    private long id;

    @Schema(description = "User's name", example = "name")
    private String name;

    @Schema(description = "User's email", example = "emai@address.com")
    private String email;

    @Schema(description = "User's password", example = "password")
    private String password;

    @Schema(description = "User's phone number", example = "phone number")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
