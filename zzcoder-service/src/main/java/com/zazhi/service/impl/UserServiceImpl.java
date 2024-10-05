package com.zazhi.service.impl;

import com.zazhi.constant.RegexConstant;
import com.zazhi.dto.LoginByEmailDTO;
import com.zazhi.dto.LoginDTO;
import com.zazhi.exception.*;
import com.zazhi.utils.JwtUtil;
import com.zazhi.utils.Md5Util;
import com.zazhi.dto.RegisterDTO;
import com.zazhi.entity.User;
import com.zazhi.mapper.UserMapper;
import com.zazhi.service.UserService;
import com.zazhi.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zazhi
 * @date 2024/8/30
 * @description: 用户相关业务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    VerificationCodeService verificationCodeService;

    @Autowired
    private RedisUtil redisUtil;

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
     * 通过用户名、手机号、邮箱查找用户
     * @param identification
     * @return
     */
    public User findUserByIdentification(String identification) {
        if (identification.matches(RegexConstant.EMAIL_REGEX)) {
            return findByEmail(identification);
        } else if (identification.matches(RegexConstant.PHONE_REGEX)) {
            log.info("在以手机号登录，{}", identification);
            return findByPhoneNumber(identification);
        } else {
            log.info("在以用户名登录，{}", identification);
            return findByUsername(identification);
        }
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
     * @param id
     * @param password
     */
    public void updatePsw(Long id, String password) {
        userMapper.updatePsw(id, Md5Util.getMD5String(password));
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
     *
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
        if(user == null || user.getPassword().equals(Md5Util.getMD5String(loginDTO.getPassword()))){
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
            throw new UserNotFoundException();
        }
        if(!verificationCodeService.verifyCode(email, code)){
            throw new VerificationCodeException();
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
}
