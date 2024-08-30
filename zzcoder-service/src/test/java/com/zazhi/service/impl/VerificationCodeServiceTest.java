package com.zazhi.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class VerificationCodeServiceTest {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Test
    void sendVerificationCode() {
        verificationCodeService.sendVerificationCode("1520197354@qq.com");

    }

    @Test
    void verifyCode(){
        System.out.println(verificationCodeService.verifyCode("2065707071@qq.com", "318113"));
    }
}