package com.zazhi.mapper;

import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.common.pojo.entity.User;
import com.zazhi.common.pojo.vo.RoleAndPermissionVO;
import com.zazhi.common.pojo.vo.UserSubmitStatVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 根据邮箱地址查找用户
     * @param email 邮箱地址
     * @return 用户对象
     */
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    /**
     * 插入新用户
     * @param user 用户对象
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
    User getById(Integer userId);

    /**
     * 更新用户的密码
     * @param id 用户ID
     * @param password 新密码
     */
    @Update("update user set password = #{password}, update_time = now() where id = #{id}")
    void updatePassword(Integer id, String password);

    /**
     * 更新用户信息通用方法
     * @param user 用户对象
     */
    void update(User user);

    /**
     * 通过角色查询权限
     * @param roles
     * @return
     */
    List<Permission> findPermissionsByRoles(List<Role> roles);

    @Select("select * from user where username = #{username}")
    User getByName(String username);

    /**
     * 获取用户通过次数
     * @param userId 用户ID
     * @return 通过次数
     */
    @Select("select " +
            "count(s.id) as total_submit, " +
            "sum(if(s.status='AC', 1, 0)) as ac_submit, " +
            "count(distinct if(s.status='AC', s.problem_id, null)) as ac_problem " +
            "from submission s where s.user_id = #{userId};")
    UserSubmitStatVO getSubmitStat(Integer userId);

    /**
     * 通过用户ID获取用户角色和权限
     * @param userId 用户ID
     * @return 角色和权限信息
     */
    RoleAndPermissionVO getRoleAndPermissionByUserId(Integer userId);
}
