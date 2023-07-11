package com.myspringboot.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuMapper {
    String getPermsByUserId = """
            SELECT DISTINCT sm.perms FROM sys_user_role sur
            LEFT JOIN sys_role sr ON sur.role_id = sr.id
            LEFT JOIN sys_role_menu srm ON srm.role_id  = sr.id
            LEFT JOIN sys_menu sm ON sm.id  = srm.menu_id
            WHERE user_id = #{id} AND sr.status = 0 AND sm.status = 0;
            """;
    @Select(getPermsByUserId)
    List<String> getPermsByUserId(@Param("id") long id);
}

// Maybe be used in future
//    @Results(id = "menuResultMap", value = {@Result(property = "menuName", column = "menu_name"),
//            @Result(property = "createdBy", column = "created_by"),
//            @Result(property = "createdTime", column = "created_time"),
//            @Result(property = "updatedBy", column = "updated_by"),
//            @Result(property = "updatedTime", column = "updated_time")
//    })
