package com.zazhi.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zazhi
 * @date 2024/12/10
 * @description: JwtToken
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
