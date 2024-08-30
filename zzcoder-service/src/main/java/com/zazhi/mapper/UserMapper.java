package com.zazhi.mapper;

import com.zazhi.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据邮箱地址查找用户
     * @param email
     * @return
     */
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    /**
     * 插入新用户
     * @param user
     */
    @Insert("insert into user(username, password, email, create_time, update_time) " +
            "values(#{username}, #{password}, #{email}, now(), now())")
    void insert(User user);
}
