package com.vueblog.controller;


import com.vueblog.common.lang.Result;
import com.vueblog.entity.User;
import com.vueblog.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Generator@Hang
 * @since 2024-01-18
 */
@RestController
//requestMapping 标注了访问这个controller的部分路径
//比如下面的用户查询 就需要输入 /user/index
//保存则输入 /user/save
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
//@Autowired注解可以让SpringBoot提供一个需要的bean。
//如果声明Autowired（自动装配）一个UserService，
// 由于UserService是一个接口，SpringBoot将挑选一个UserService的实现返回给用户，在项目中就是UserServiceImpl。
    @RequiresAuthentication
    @GetMapping("/index")
    //测试：将id为1的用户查出来
    public Result index(){
        User user = userService.getById(1L);
        return Result.succ(user);
    }
    @PostMapping("/save")//validated表示开启对user的校验
    public Result save(@Validated @RequestBody User user) {
        return Result.succ(user);
    }


}
