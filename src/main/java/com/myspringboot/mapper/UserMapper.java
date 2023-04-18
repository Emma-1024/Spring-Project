package com.myspringboot.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.myspringboot.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // Mapper defined method
    LocalDateTime now();

    @Select("SELECT * FROM users")
    List<User> getAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM users WHERE name = #{name}")
    User getByName(@Param("name") String name);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(@Param("id") long id);

    @Insert("INSERT INTO users(name,email,password, phone_number) " +
            " VALUES (#{name}, #{email}, #{password},#{phone_number})")
    int insert(User item);

    @Update("Update users set name=#{name}, " +
            " password=#{password} where id=#{id}")
    int updateById(@Param("id") Long id, @Param("name") String name, @Param("password") String password);
}
