package com.vueblog.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.vueblog.common.lang.Result;
import com.vueblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//这个类的作用：拦截所有请求，判断请求头中是否有jwt，如果有，进入realm进行登录校验（shiro在realm中查找用户的权限数据），如果没有，直接放行
@Component
public class JwtFilter extends AuthenticatingFilter {//AuthenticatingFilter，一个可以内置了可以自动登录方法的的过滤器

    @Autowired
    JwtUtils jwtUtils;

    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
// HttpServletRequest对象代表客户端的请求 当客户端用过HTTP协议访问服务器是 HTTP请求头所有信息都在这个对象中
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String jwt = request.getHeader("Authorization");//返回指定请求头的值
        if(StringUtils.isBlank(jwt)){//pom中的导入包存有工具错误，找不到正确的包，用isBlank判断结果不会有影响
            return null;
        }

        return new JwtToken(jwt);//将jwtToken封装为对应对象 方便调用
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //强制转换为HTTP请求
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isBlank(jwt)){//pom中的导入包存有工具错误，找不到正确的包，用isBlank判断结果不会有影响
            //若没有jwt，直接放行
            return true;
        }else{

            //校验jwt是否可用 使用jwt中的getclaim方法得到用户数据
            Claims claim = jwtUtils.getClaimByToken(jwt);
            //如果claim为空或者claim过期，抛出异常
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())){
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            //登录处理

            return executeLogin(servletRequest,servletResponse);
        }
    }

    /***
     * 登录异常时候进入的方法，我们直接把异常信息封装然后抛出
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    //重写onLoginFailure方法，登录失败时返回json数据，因为是前后端分离，所以返回json数据（与result类格式统一）
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,ServletRequest request, ServletResponse response){

        HttpServletResponse httpServletResponse =  (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().println(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return false;
    }
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
