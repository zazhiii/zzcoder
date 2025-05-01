package com.zazhi.service.impl;

import com.zazhi.constant.MsgConstant;
import com.zazhi.dto.*;
import com.zazhi.entity.Permission;
import com.zazhi.entity.Role;
import com.zazhi.exception.*;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.AuthService;
import com.zazhi.utils.JwtUtil;
import com.zazhi.utils.Md5Util;
import com.zazhi.entity.User;
import com.zazhi.utils.RedisUtil;
import com.zazhi.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 登录鉴权相关业务
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthMapper authMapper;

    @Autowired
    VerificationCodeService verificationCodeService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 通过邮箱查找用户
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    /**
     * 通过手机号查询用户
     * @param phoneNumber
     * @return
     */
    public User findByPhoneNumber(String phoneNumber) {
        return userMapper.findByPhoneNumber(phoneNumber);
    }

    /**
     * 通过id查询用户
     *
     * @param userId
     * @return
     */
    public User findUserById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 更新用户的密码
     * @param updatePasswordDTO
     * @param token
     */
    public void updatePsw(UpdatePasswordDTO updatePasswordDTO, String token) {
        String oldPassword = updatePasswordDTO.getOldPassword();
        String newPassword = updatePasswordDTO.getNewPassword();
        // 判断原密码是否正确
        Long userId = ThreadLocalUtil.getCurrentId();
        User user = userMapper.findById(userId);
        if(!Md5Util.getMD5String(oldPassword).equals(user.getPassword())){
            throw new InvalidCredentialsException(MsgConstant.ORIGINAL_PASSWORD_INCORRECT);
        }
        // 删除旧token
        redisUtil.delete(token);
        // 更新密码
        userMapper.updatePsw(userId, Md5Util.getMD5String(newPassword));
    }

    /**
     * 用户注册
     * @param registerDTO
     */
    public void register(RegisterDTO registerDTO) {
        //判断邮箱是否注册
        User user = userMapper.findByEmail(registerDTO.getEmail());
        if(user != null){
            throw new EmailAlreadyRegisteredException();
        }

        // 判断用户名是否注册
        user = userMapper.findByUsername(registerDTO.getUsername());
        if(user != null){
            throw new UsernameAlreadyRegisteredException();
        }

        //判断验证码是否正确
        if(!verificationCodeService.verifyCode(registerDTO.getEmail(), registerDTO.getEmailVerificationCode())){
            throw new VerificationCodeException();
        }

        user = new User();
        BeanUtils.copyProperties(registerDTO, user); // 拷贝属性
        user.setPassword(Md5Util.getMD5String(user.getPassword())); // 加密密码
        userMapper.insert(user);
    }

    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    public String login(LoginDTO loginDTO) {
        String identification = loginDTO.getIdentification();
        // 根据用户输入的用户名或邮箱或手机号查找用户
        User user = userMapper.findByUsername(identification);
        if(user == null){
            user = userMapper.findByEmail(identification);
        }
        if(user == null){
            user = userMapper.findByPhoneNumber(identification);
        }

        // 用户名或者密码错误
        if(user == null || !user.getPassword().equals(Md5Util.getMD5String(loginDTO.getPassword()))){
            throw new InvalidCredentialsException();
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        // 登录后 token 存入redis {token: token}的形式
        redisUtil.set(token, token, 7, TimeUnit.DAYS);
        return token;
    }

    /**
     * 通过邮箱验证码登录
     * @param loginByEmailDTO
     * @return
     */
    public String loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        String email = loginByEmailDTO.getEmail();
        String code = loginByEmailDTO.getEmailVerificationCode();
        User user = userMapper.findByEmail(email);

        // 判断邮箱和验证码是否正确
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        if(!verificationCodeService.verifyCode(email, code)){
            throw new RuntimeException("验证码错误");
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);
        // token 存入redis
        redisUtil.set(token, token, 7, TimeUnit.DAYS);

        return token;
    }

    /**
     * 通过邮箱验证码更新密码
     * @param updatePasswordByEmailDTO
     */
    public void updatePswByEmail(UpdatePasswordByEmailDTO updatePasswordByEmailDTO) {
        String email = updatePasswordByEmailDTO.getEmail();
        String code = updatePasswordByEmailDTO.getEmailVerificationCode();
        // 判断用户是否存在
        User user = userMapper.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException();
        }
        // 判断验证码是否正确
        if(!verificationCodeService.verifyCode(email, code)){
            throw new VerificationCodeException();
        }

        String newPassword = updatePasswordByEmailDTO.getNewPassword();
        userMapper.updatePsw(user.getId(), Md5Util.getMD5String(newPassword));
    }

    /**
     * 添加角色
     * @param roleName
     */
    public void addRole(String roleName, String description) {
        Role role = authMapper.getRoleByName(roleName);
        if(role != null){
            throw new RuntimeException("角色已存在");
        }
        role = Role.builder()
                .name(roleName)
                .description(description)
                .build();
        authMapper.addRole(role);
    }

    /**
     * 更新角色信息
     * @param role
     */
    public void updateRole(Role role) {
       authMapper.updateRole(role);
    }

    /**
     * 删除角色
     * @param id
     */
    public void deleteRole(Integer id) {
       authMapper.deleteRole(id);
    }

    /**
     * 获取所有角色
     * @return
     */
    public List<Role> getRoles() {
        return authMapper.getRoles();
    }

    /**
     * 添加权限到角色
     * @param roleId
     * @param permissionId
     */
    public void addPermissionToRole(Integer roleId, Integer permissionId) {
        authMapper.addPermissionToRole(roleId, permissionId);
    }

    /**
     * 添加角色到用户
     * @param roleId
     * @param userId
     */
    public void addRoleToUser(Integer roleId, Long userId) {
       authMapper.addRoleToUser(roleId, userId);
    }

    /**
     * 获取所有权限
     * @return
     */
    public List<Permission> getPermissions() {
        return authMapper.getAllPermissions();
    }
}
