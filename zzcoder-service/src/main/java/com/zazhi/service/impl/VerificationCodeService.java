package com.zazhi.service.impl;

import com.zazhi.utils.RedisUtil;
import com.zazhi.exception.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    private static final String VERIFICATION_CODE_PREFIX = "email_verification_code:";
    private static final int CODE_EXPIRY = 5 * 60; // 验证码有效期 5 分钟

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 生成随机验证码
    private String generateVerificationCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000)); // 6位随机数字
    }

    // 发送邮件
    private void sendEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + verificationCode + ". It will expire in 5 minutes.");
        mailSender.send(message);
    }

    // 发送验证码逻辑
    public void sendVerificationCode(String email) {
        // 1. 生成验证码
        String verificationCode = generateVerificationCode();

        String redisKey = VERIFICATION_CODE_PREFIX + email;

        // 若键以存在则判断为操作频繁
        if(redisUtil.hasKey(redisKey)){
            throw new TooManyRequestsException("操作过快，请" + redisUtil.getExpire(redisKey) + "秒后重试！");
        }

        // 2. 存储验证码到 Redis
        redisUtil.set(redisKey, verificationCode, CODE_EXPIRY, TimeUnit.SECONDS);

        // 3. 发送邮件
        sendEmail(email, verificationCode);
    }

    // 验证验证码逻辑
    public boolean verifyCode(String email, String code) {
        String redisKey = VERIFICATION_CODE_PREFIX + email;
        String storedCode = redisUtil.get(redisKey);
        return storedCode != null && storedCode.equals(code);
    }
}
