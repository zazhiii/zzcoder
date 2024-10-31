package com.zazhi.mapper;

import com.zazhi.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthMapper {
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

    /**
     * 通过手机号查找用户
     *
     * @param phoneNumber
     * @return
     */
    @Select("select * from user where phone_number = #{phoneNumber}")
    User findByPhoneNumber(String phoneNumber);

    /**
     * 通过id查询用户
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User findById(Long userId);

    /**
     * 更新用户的密码
     * @param id
     * @param password
     */
    @Update("update user set password = #{password}, update_time = now() where id = #{id}")
    void updatePsw(Long id, String password);
}
