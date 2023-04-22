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

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Insert("INSERT INTO users(name, email, password, phone_number) " +
            " VALUES (#{name}, #{email}, #{password}, #{phoneNumber})")
    int insert(User item);

    @Update("Update users set name=#{name}, email=#{email}, phone_number=#{phoneNumber}, " +
            "password=#{password} where id=#{id}")
    int update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(@Param("id") long id);
}
