package com.vueblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueblog.common.dto.LoginDto;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.User;
import com.vueblog.service.UserService;
import com.vueblog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: AccountController
 * Package: com.vueblog.controller
 * Description:
 *  登录控制器 登录接口
 * @Author lisiyi
 * @Creat 2024/1/20 23:33
 */


@RestController
//如果直接使用@Controller，返回的值会是一个网页，而使用@RestController，返回的就是我们想要的具体数据
// 而前后端分离的项目，前后端之间传递的正是JSON类型的数值
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")//登录没有使用shiro的注解，因为shiro的注解是用来做权限控制的
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        //先从数据库中查询是否有这个用户 userService就是用来访问数据库的
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        //密码是否等于用户输入的加密内容
        if(!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }
        //上面两步都通过 就对该用户产生jwtToken
        String jwt = jwtUtils.generateToken(user.getId());
        //对回复的消息头中的Authorization键 对应值设置为jwtToken
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        //返回用户信息
        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    @RequiresAuthentication//需要认证
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();//shiro的退出登录 subject可以视作shiro中的用户
        return Result.succ(null);
    }

}
