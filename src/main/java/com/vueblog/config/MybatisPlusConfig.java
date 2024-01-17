package com.vueblog.config;

//import com.baomidou.mybatisplus.extension.plugins.;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//"""
//通过@mapperScan注解指定要变成实现类的接口所在的包
// 然后包下面的所有接口在编译之后都会生成相应的实现类。
// PaginationInterceptor是一个分页插件。
//"""
//@Configuration
//@EnableTransactionManagement
//@SpringBootApplication
//指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
//添加位置：是在Springboot启动类上面添加
//@MapperScan("com.vueblog.mapper")
//public class MybatisPlusConfig {
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        return paginationInterceptor;
//    }

//}