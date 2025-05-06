package com.zazhi.mapper;

import com.zazhi.pojo.entity.Permission;
import com.zazhi.pojo.entity.Role;
import com.zazhi.pojo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    @Insert("insert into user(username, password, email) " +
            "values(#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
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

    /**
     * 更新用户信息通用方法
     * @param user
     */
    void update(User user);

    /**
     * 通过用户id查询用户角色
     * @param userId
     * @return
     */
    @Select("select r.* from user_role ur left join role r on ur.role_id = r.id where ur.user_id = #{userId}")
    List<Role> getUserRolesById(Long userId);

    /**
     * 通过角色查询权限
     * @param roles
     * @return
     */
    List<Permission> findPermissionsByRoles(List<Role> roles);

    @Select("select * from user where username = #{username}")
    User getByName(String username);
}
