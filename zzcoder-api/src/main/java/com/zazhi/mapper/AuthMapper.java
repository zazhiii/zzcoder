package com.zazhi.mapper;


import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuthMapper {

    /**
     * 添加角色
     * @param role
     */
    @Insert("insert into role (name, description) values (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addRole(Role role);

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
    List<Permission> getAllPermissions();

    /**
     * 根据角色名称查询角色
     * @param roleName
     * @return
     */
    @Select("select * from role where name = #{roleName}")
    Role getRoleByName(String roleName);

    /**
     * 判断角色是否有权限
     * @param roleId
     * @param permissionId
     * @return
     */
    @Select("select count(*) > 0 from role_permission where role_id = #{roleId} and permission_id = #{permissionId}")
    Boolean roleHasPermission(Integer roleId, Integer permissionId);

    /**
     * 根据角色ID获取权限
     * @param roleId
     * @return
     */
    @Select("select p.* from role_permission rp left join permission p on rp.permission_id = p.id where rp.role_id = #{roleId}")
    List<Permission> getPermissionsByRoleId(Integer roleId);

    /**
     * 查看用户是否有角色
     * @param userId
     * @param roleId
     * @return
     */
    @Select("select count(*) > 0 from user_role where user_id = #{userId} and role_id = #{roleId}")
    Boolean userHasRole(Integer userId, Integer roleId);
}
