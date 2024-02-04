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
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

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
