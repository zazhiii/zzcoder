package com.zazhi.zzcoder.mapper;


import com.zazhi.zzcoder.common.pojo.entity.Permission;
import com.zazhi.zzcoder.common.pojo.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuthMapper {

    /**
     * 添加角色
     * @param role 角色
     */
    @Insert("insert into role (name, description) values (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addRole(Role role);

    /**
     * 更新角色描述
     * @param description 角色描述
     * @param id 角色ID
     */
    @Update("update role set description = #{description} where id = #{id}")
    void updateRole(String description, Integer id);

    /**
     * 删除角色
     * @param id 角色ID
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
     * @param roleName 角色名称
     * @return 角色
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

    /**
     * 根据角色ID获取用户ID列表
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    @Select("select user_id from user_role where role_id = #{roleId}")
    List<Integer> getUserIdsByRoleId(Integer roleId);

    /**
     * 删除用户的角色
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    @Delete("delete from user_role where role_id = #{roleId} and user_id = #{userId}")
    void deleteRoleFromUser(Integer roleId, Integer userId);

    /**
     * 删除角色的权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    @Delete("delete from role_permission where role_id = #{roleId} and permission_id = #{permissionId}")
    void deletePermissionFromRole(Integer roleId, Integer permissionId);
}
