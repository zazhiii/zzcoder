package com.zazhi.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送简单文本邮件
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送 HTML 邮件
     */
    public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // 第二个参数为 true 表示发送 HTML
        mailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     */
    public void sendMailWithAttachment(String to, String subject, String content, File attachment) throws MessagingException {
        if (attachment == null || !attachment.exists()) {
            throw new IllegalArgumentException("附件不存在");
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, false);
        helper.addAttachment(attachment.getName(), attachment);
        mailSender.send(message);
    }

    /**
     * 发送验证码邮件
     */
    public void sendVerificationCode(String to, String code) {
        String subject = "【ZZCoder】您的验证码";
        String content = "您好，您的验证码是：" + code + "，有效期为5分钟，请及时使用。";
        sendSimpleMail(to, subject, content);
    }
}
