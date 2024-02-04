package com.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private  String token;

    public JwtToken(String jwt){
        this.token = jwt;
    }
    @Override//类似用户名
    public Object getPrincipal() {
        return token;
    }

    @Override//类似密码
    public Object getCredentials() {
        return token;
    }
}
