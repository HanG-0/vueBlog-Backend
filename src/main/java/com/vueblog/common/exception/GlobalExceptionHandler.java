package com.vueblog.common.exception;

import com.vueblog.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//全局异常处理
//有时候不可避免服务器报错的情况，如果不配置异常处理机制，
// 就会默认返回tomcat或者nginx的5XX页面，对普通用户来说，不太友好，
// 用户也不懂什么情况
// 这时候需要我们程序员设计返回一个友好简单的格式给前端。
@Slf4j
//异步
@RestControllerAdvice
public class GlobalExceptionHandler {
    //shiro异常处理
    @ResponseStatus(HttpStatus.UNAUTHORIZED)//shiro异常 无权限401状态码

    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e){
        //日志输出
        log.error("运行时异常：----------------");
        return Result.fail(401,e.getMessage(),null);
    }
//实体校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：----------------{}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail(objectError.getDefaultMessage());
    }
    //assert异常 用户名为null
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常：----------------{}", e);
        return Result.fail(e.getMessage());
    }
    //runtime异常处理
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常：----------------");
        System.out.println(e.getMessage());
        return Result.fail(e.getMessage());
    }

}
