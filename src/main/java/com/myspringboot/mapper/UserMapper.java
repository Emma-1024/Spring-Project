package com.myspringboot.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.myspringboot.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // Mapper defined method
    LocalDateTime now();

    @Results(id = "userResultMap", value = {
            @Result(property = "phoneNumber", column = "phone_number")
    })
    @Select("SELECT * FROM users")
    List<User> getAll();

    @ResultMap("userResultMap")
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getById(@Param("id") long id);

    @ResultMap("userResultMap")
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

    @Delete("DELETE FROM users")
    int deleteAll();
}
