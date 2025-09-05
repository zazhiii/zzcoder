package com.zazhi.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zazhi.common.constants.RedisKeyConstants;
import com.zazhi.common.enums.EmailCodeBizType;
import com.zazhi.common.exception.code.AuthError;
import com.zazhi.common.exception.model.BizException;
import com.zazhi.common.pojo.dto.*;
import com.zazhi.common.utils.*;
import com.zazhi.config.properties.EmailCodeProperties;
import com.zazhi.common.pojo.entity.Permission;
import com.zazhi.common.pojo.entity.Role;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.AuthService;
import com.zazhi.common.pojo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zazhi.common.constants.RedisKeyConstants.*;
import static com.zazhi.common.exception.code.AuthErrorCode.*;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 登录鉴权相关业务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    private final AuthMapper authMapper;

    private final RedisUtil redisUtil;

    private final TemplateEngine templateEngine;

    private final MailUtil mailUtil;

    private final EmailCodeProperties emailCodeProperties;

    /**
     * 更新用户的密码
     *
     * @param updatePasswordDTO 用户输入的旧密码和新密码
     * @param token             用户的token
     */
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO, String token) {
        String oldPassword = updatePasswordDTO.getOldPassword();
        String newPassword = updatePasswordDTO.getNewPassword();
        // 判断原密码是否正确
        Integer userId = ThreadLocalUtil.getCurrentId();
        User user = userMapper.findById(userId);
        if (!DigestUtil.md5Hex(oldPassword).equals(user.getPassword())) {
            throw new BizException(AuthError.ORIGINAL_PASSWORD_INCORRECT);
        }
        // 更新密码之后，主动使旧token失效
        redisUtil.delete(RedisKeyConstants.format(JWT_TOKEN, userId));
        userMapper.updatePassword(userId, DigestUtil.md5Hex(newPassword));
    }

    /**
     * 用户注册
     *
     * @param registerDTO 用户注册信息
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        //判断邮箱是否注册
        User user = userMapper.findByEmail(registerDTO.getEmail());
        if (user != null) {
            throw new BizException(EMAIL_EXISTS);
        }

        // 判断用户名是否注册
        user = userMapper.findByUsername(registerDTO.getUsername());
        if (user != null) {
            throw new BizException(USERNAME_EXISTS);
        }

        // 判断验证码是否正确
        String key = RedisKeyConstants.format(REGISTER_EMAIL_CODE, registerDTO.getEmail());
        String storeCode = redisUtil.get(key);
        if (storeCode == null) {
            throw new BizException(CAPTCHA_EXPIRED);
        }
        if (!storeCode.equals(registerDTO.getEmailCode())) {
            throw new BizException(CAPTCHA_INCORRECT);
        }

        user = new User();
        BeanUtils.copyProperties(registerDTO, user); // 拷贝属性
        user.setPassword(DigestUtil.md5Hex(user.getPassword())); // 加密密码
        userMapper.insert(user);
    }

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return token
     */
    public String login(LoginDTO loginDTO) {
        String identification = loginDTO.getIdentification();
        // 根据用户输入的用户名或邮箱或手机号查找用户
        User user = userMapper.findByUsername(identification);
        if (user == null) {
            user = userMapper.findByEmail(identification);
        }
        if (user == null) {
            user = userMapper.findByPhoneNumber(identification);
        }

        // 用户名或者密码错误
        String md5Pwd = DigestUtil.md5Hex(loginDTO.getPassword());
        if (user == null || !user.getPassword().equals(md5Pwd)) {
            throw new BizException(PASSWORD_INCORRECT);
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        // 存入 redis 的目的是能够主动让 token 失效
        String key = RedisKeyConstants.format(JWT_TOKEN, user.getId());
        redisUtil.set(key, token, 7, TimeUnit.DAYS);
        return token;
    }

    /**
     * 通过邮箱验证码登录
     *
     * @param loginByEmailDTO 邮箱、验证码
     * @return token
     */
    public String loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        String email = loginByEmailDTO.getEmail();
        String code = loginByEmailDTO.getEmailCode();
        User user = userMapper.findByEmail(email);

        // 判断邮箱和验证码是否正确
        if (user == null) {
            throw new BizException(USER_NOT_FOUND);
        }

        // 验证码校验
        String key = RedisKeyConstants.format(EMAIL_CODE, EmailCodeBizType.LOGIN.getCode(), email);
        String storeCode = redisUtil.get(key);
        if (storeCode == null || !storeCode.equals(code)) {
            throw new BizException(CAPTCHA_INCORRECT);
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        // 存入 redis
        String tokenKey = RedisKeyConstants.format(JWT_TOKEN, user.getId());
        redisUtil.set(tokenKey, token, 1, TimeUnit.DAYS);
        return token;
    }

    /**
     * 通过邮箱验证码更新密码
     *
     * @param updatePasswordByEmailDTO 邮箱、验证码、新密码
     */
    public void updatePasswordByEmail(UpdatePasswordByEmailDTO updatePasswordByEmailDTO) {
        String email = updatePasswordByEmailDTO.getEmail();
        String code = updatePasswordByEmailDTO.getEmailCode();
        // 判断用户是否存在
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new BizException(USER_NOT_FOUND);
        }

        // 验证码校验
        String key = RedisKeyConstants.format(EMAIL_CODE, EmailCodeBizType.RESET_PASSWORD.getCode(), email);
        String storeCode = redisUtil.get(key);
        if (storeCode == null || !storeCode.equals(code)) {
            throw new BizException(CAPTCHA_INCORRECT);
        }

        String newPassword = updatePasswordByEmailDTO.getNewPassword();
        userMapper.updatePassword(user.getId(), DigestUtil.md5Hex(newPassword));
    }

    /**
     * 添加角色
     *
     * @param roleName 角色名
     */
    @Override
    public void addRole(String roleName, String description) {
        Role role = authMapper.getRoleByName(roleName);
        if (role != null) {
            throw new BizException(AuthError.ROLE_EXISTS);
        }
        role = Role.builder()
                .name(roleName)
                .description(description)
                .build();
        authMapper.addRole(role);
    }

    /**
     * 更新角色信息
     *
     * @param description 角色描述
     * @param id          角色ID
     */
    @Override
    public void updateRoleDesc(String description, Integer id) {
        authMapper.updateRole(description, id);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    public void deleteRole(Integer id) {
        authMapper.deleteRole(id);
        // 删除相关用户的缓存
        List<Integer> userIds = authMapper.getUserIdsByRoleId(id);
        deleteRolePermissionCache(userIds);
    }

    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    public List<Role> getRoles() {
        return authMapper.getRoles();
    }

    /**
     * 添加权限到角色
     *
     * @param roleId  角色ID
     * @param permissionId 权限ID
     */
    public void addPermissionToRole(Integer roleId, Integer permissionId) {
        authMapper.addPermissionToRole(roleId, permissionId);
        // 删除相关用户的缓存
        List<Integer> userIds = authMapper.getUserIdsByRoleId(roleId);
        deleteRolePermissionCache(userIds);
    }

    /**
     * 添加角色到用户
     *
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    public void addRoleToUser(Integer roleId, Integer userId) {
        authMapper.addRoleToUser(roleId, userId);
        // 删除用户的缓存
        deleteRolePermissionCache(List.of(userId));
    }

    /**
     * 获取所有权限
     *
     * @return 权限列表
     */
    public List<Permission> getPermissions() {
        return authMapper.getAllPermissions();
    }

    /**
     * 发送邮箱验证码
     *
     * @param sendCodeDTO 发送信息
     */
    @Override
    public void sendEmailCode(SendCodeDTO sendCodeDTO) {
        // 校验业务类型
        if (!EmailCodeBizType.isValid(sendCodeDTO.getBusinessType())) {
            throw new BizException(AuthError.INVALID_BUSINESS_TYPE);
        }

        String code = RandomUtil.randomNumbers(emailCodeProperties.getLength());

        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("appName", "ZZCoder");
        context.setVariable("expire", emailCodeProperties.getExpire());

        String templateName = switch (EmailCodeBizType.fromCode(sendCodeDTO.getBusinessType())) {
            case REGISTER -> "email/register-code.html";
            case LOGIN -> "email/login-code.html";
            case RESET_PASSWORD -> "email/reset-password-code.html";
            case CHANGE_EMAIL -> "email/change-email-code.html";
            case UPDATE_EMAIL -> "email/update-email-code.html";
        };

        String htmlContent = templateEngine.process(templateName, context);

        try {
            mailUtil.sendHtmlMail(sendCodeDTO.getEmail(), "【ZZCoder】您的验证码", htmlContent);
        } catch (Exception e) {
            throw new BizException(AuthError.EMAIL_SEND_FAIL);
        }

        String key = RedisKeyConstants.format(EMAIL_CODE, sendCodeDTO.getBusinessType(), sendCodeDTO.getEmail());
        redisUtil.set(key, code, emailCodeProperties.getExpire(), TimeUnit.MINUTES);
    }

    /**
     * 登出
     *
     * @param token 用户token
     */
    @Override
    public void logout(String token) {
        String tokenKey = RedisKeyConstants.format(JWT_TOKEN, ThreadLocalUtil.getCurrentId());
        redisUtil.delete(tokenKey);
    }

    @Override
    public void removeRoleFromUser(Integer roleId, Integer userId) {
        authMapper.deleteRoleFromUser(roleId, userId);
        // 删除用户的缓存
        deleteRolePermissionCache(List.of(userId));
    }

    @Override
    public void removePermissionFromRole(Integer roleId, Integer permissionId) {
        authMapper.deletePermissionFromRole(roleId, permissionId);
        // 删除相关用户的缓存
        List<Integer> userIds = authMapper.getUserIdsByRoleId(roleId);
        deleteRolePermissionCache(userIds);
    }

    private void deleteRolePermissionCache(List<Integer> userIds) {
        for (Integer userId : userIds) {
            String key = RedisKeyConstants.format(USER_ROLE_PERMISSIONS, userId);
            redisUtil.delete(key);
        }
    }
}
