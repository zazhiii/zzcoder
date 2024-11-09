package com.zazhi.mapper;


import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuthMapper {

    /**
     * 添加角色
     * @param roleName
     */
    @Insert("insert into role (name, description) values (#{roleName}, #{description})")
    void addRole(String roleName, String description);

    /**
     * 删除角色
     * @param role
     */
    @Update("update role set name = #{name}, description = #{description} where id = #{id}")
    void updateRole(Role role);

    /**
     * 删除角色
     * @param id
     */
    @Delete("delete from role where id = #{id}")
    void deleteRole(Integer id);

    /**
     * 获取所有角色
     * @return
     */
    @Select("select * from role")
    List<Role> getRoles();

    /**
     * 添加权限到角色
     * @param roleId
     * @param permissionId
     */
    @Insert("insert into role_permission (role_id, permission_id) values (#{roleId}, #{permissionId})")
    void addPermissionToRole(Integer roleId, Integer permissionId);

    /**
     * 添加角色到用户
     * @param roleId
     * @param userId
     */
    @Insert("insert into user_role (role_id, user_id) values (#{roleId}, #{userId})")
    void addRoleToUser(Integer roleId, Integer userId);

    /**
     * 获取所有权限
     * @return
     */
    @Select("select * from permission")
    List<Permission> getPermissions();
}
