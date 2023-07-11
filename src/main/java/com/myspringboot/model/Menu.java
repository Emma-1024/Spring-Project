package com.myspringboot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Schema(description = "Menu Model Information")
@Data
@NoArgsConstructor
public class Menu {
    private long id;
    private String menuName;
    private Boolean status;
    private String perms;
    private String createdBy;
    private ZonedDateTime createdTime;
    private String updatedBy;
    private ZonedDateTime updatedTime;
}
