package com.zazhi.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zazhi.common.constant.MsgConstant;
import com.zazhi.common.constant.RedisKeyConstants;
import com.zazhi.common.enums.EmailCodeBusinessType;
import com.zazhi.common.utils.*;
import com.zazhi.config.properties.VerifyCodeProperties;
import com.zazhi.exception.model.*;
import com.zazhi.pojo.dto.*;
import com.zazhi.pojo.entity.Permission;
import com.zazhi.pojo.entity.Role;
import com.zazhi.mapper.AuthMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.AuthService;
import com.zazhi.pojo.entity.User;
import jakarta.mail.MessagingException;
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

import static com.zazhi.common.constant.ExceptionMsgConstants.EMAIL_SEND_FAIL;
import static com.zazhi.common.constant.RedisKeyConstants.JWT_TOKEN;
import static com.zazhi.common.enums.AuthErrorCode.*;

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

    private final VerificationCodeService verificationCodeService;

    private final RedisUtil redisUtil;

    private final TemplateEngine templateEngine;

    private final MailUtil mailUtil;

    private final VerifyCodeProperties verifyCodeProperties;

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
        if(!DigestUtil.md5Hex(oldPassword).equals(user.getPassword())){
            throw new InvalidCredentialsException(MsgConstant.ORIGINAL_PASSWORD_INCORRECT);
        }
        // 删除旧token
        redisUtil.delete(token);
        // 更新密码
        userMapper.updatePsw(userId, DigestUtil.md5Hex(newPassword));
    }

    /**
     * 用户注册
     * @param registerDTO
     */
    public void register(RegisterDTO registerDTO) {
        //判断邮箱是否注册
        User user = userMapper.findByEmail(registerDTO.getEmail());
        if(user != null){
            throw new AuthException(EMAIL_EXISTS);
        }

        // 判断用户名是否注册
        user = userMapper.findByUsername(registerDTO.getUsername());
        if(user != null){
            throw new AuthException(USERNAME_EXISTS);
        }

        // 判断验证码是否正确
        String key = RedisKeyConstants.format(RedisKeyConstants.EMAIL_CODE,
                EmailCodeBusinessType.REGISTER.getCode() + ":" + registerDTO.getEmail());
        String storeCode = redisUtil.get(key);
        if(storeCode == null || !storeCode.equals(registerDTO.getEmailCode())){
            throw new AuthException(CAPTCHA_INCORRECT);
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

        String md5Pwd = DigestUtil.md5Hex(loginDTO.getPassword());

        // 用户名或者密码错误
        if(user == null || !user.getPassword().equals(md5Pwd)){
            throw new AuthException(PASSWORD_INCORRECT);
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        String key = JWT_TOKEN + user.getId();
        redisUtil.set(key, token, 7, TimeUnit.DAYS);
        return token;
    }

    /**
     * 通过邮箱验证码登录
     * @param loginByEmailDTO
     * @return
     */
    public String loginByEmail(LoginByEmailDTO loginByEmailDTO) {
        String email = loginByEmailDTO.getEmail();
        String code = loginByEmailDTO.getEmailCode();
        User user = userMapper.findByEmail(email);

        // 判断邮箱和验证码是否正确
        if(user == null){
            throw new AuthException(USER_NOT_FOUND);
        }

        // 生成token
        String key = RedisKeyConstants.format(RedisKeyConstants.EMAIL_CODE,
                EmailCodeBusinessType.LOGIN.getCode()+ ":" + loginByEmailDTO.getEmail());
        String storeCode = redisUtil.get(key);
        if(storeCode == null || !storeCode.equals(code)){
            throw new AuthException(CAPTCHA_INCORRECT);
        }

        // 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.genToken(claims);

        String tokenKey = JWT_TOKEN + user.getId();
        redisUtil.set(tokenKey, token, 7, TimeUnit.DAYS);
        return token;
    }

    /**
     * 通过邮箱验证码更新密码
     * @param updatePasswordByEmailDTO
     */
    public void updatePswByEmail(UpdatePasswordByEmailDTO updatePasswordByEmailDTO) {
        String email = updatePasswordByEmailDTO.getEmail();
        String code = updatePasswordByEmailDTO.getEmailCode();
        // 判断用户是否存在
        User user = userMapper.findByEmail(email);
        if(user == null){
            throw new AuthException(USER_NOT_FOUND);
        }

        String key = RedisKeyConstants.format(RedisKeyConstants.EMAIL_CODE,
                EmailCodeBusinessType.RESET_PASSWORD.getCode()+ ":" + updatePasswordByEmailDTO.getEmail());
        String storeCode = redisUtil.get(key);
        if(storeCode == null || !storeCode.equals(code)){
            throw new AuthException(CAPTCHA_INCORRECT);
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

    /**
     * 发送邮箱验证码
     * @param sendCodeDTO
     */
    @Override
    public void sendEmailCode(SendCodeDTO sendCodeDTO) {
        if(!EmailCodeBusinessType.isValid(sendCodeDTO.getBusinessType())){
            throw new AuthException(INVALID_BUSINESS);
        }

        String code = RandomUtil.randomNumbers(verifyCodeProperties.getLength());

        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("appName", "ZZCoder");
        context.setVariable("expire", verifyCodeProperties.getExpire());

        String templateName = switch (EmailCodeBusinessType.fromCode(sendCodeDTO.getBusinessType())) {
            case REGISTER -> "email/register-code.html";
            case LOGIN -> "email/login-code.html";
            case RESET_PASSWORD -> "email/reset-password-code.html";
            case CHANGE_EMAIL -> "email/change-email-code.html";
        };

        String htmlContent = templateEngine.process(templateName, context);

        try {
            mailUtil.sendHtmlMail(sendCodeDTO.getEmail(), "【ZZCoder】您的验证码", htmlContent);
        } catch (Exception e) {
            throw new BizException(EMAIL_SEND_FAIL);
        }

        String key = RedisKeyConstants.format(RedisKeyConstants.EMAIL_CODE, sendCodeDTO.getBusinessType() + ":" + sendCodeDTO.getEmail());
        redisUtil.set(key, code, verifyCodeProperties.getExpire(), TimeUnit.MINUTES);
    }

    /**
     * 登出
     * @param token
     */
    @Override
    public void logout(String token) {
        String tokenKey = JWT_TOKEN + ":" + ThreadLocalUtil.getCurrentId();
        redisUtil.delete(tokenKey);
    }

}
