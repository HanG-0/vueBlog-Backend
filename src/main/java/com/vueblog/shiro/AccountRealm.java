package com.vueblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.vueblog.entity.User;
import com.vueblog.service.UserService;
import com.vueblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//自定义realm 构建securityManager环境
//权限信息验证 完成shiro的认证（Authorization）和授权(Authentication) 也就是完成这两个方法
@Component
public class AccountRealm extends AuthorizingRealm {
//注入jwt工具类
     @Autowired
    JwtUtils jwtUtils;
//注入userService
     @Autowired
    UserService userService;

    /**
     *
     * @param token 使得该Realm是专门用来认证jwtToken的
     * @return
     */
     @Override
     public boolean supports(AuthenticationToken token){
         //
         return token instanceof JwtToken;
     }


     //授权访问控制，用于对用户的操作授权，证明用户是否有权限做某件事
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        return null;
    }
//验证用户身份
//shiro收到jwtToken jwtToken携带用户id 通过jwt工具类解析出用户id
    //密码校验等逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//强转为JwtToken
        JwtToken jwtToken = (JwtToken)token;
//get principal得到jwtToken  通过claim获取userid
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
//通过userService在数据库查出用户
        User user = userService.getById(Long.valueOf(userId));
//判断用户是否可用
        if (user == null){
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1){
            throw new LockedAccountException("账户已被锁定");
        }
//将user信息复制给AccountProfile
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        System.out.println("-------------");
//用户信息 账号密码各种信息 还有realm的类名 返回给shiro
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(),getName());
    }
}
