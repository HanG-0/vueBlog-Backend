package com.vueblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题 配置到controller上的
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {//WebMvcConfigurer是一个接口，实现这个接口，重写addCorsMappings方法
    @Override
    public void addCorsMappings(CorsRegistry registry) {//跨域配置
        registry.addMapping("/**")//允许所有的请求路径
                .allowedOriginPatterns("*")//允许所有的请求来源
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")//允许所有的请求方法
                .allowCredentials(true)//允许携带cookie
                .maxAge(3600)//有效期
                .allowedHeaders("*");//允许所有的请求头
    }
}
