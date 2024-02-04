package com.vueblog.config;

import com.vueblog.shiro.AccountRealm;
import com.vueblog.shiro.JwtFilter;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//配置shiro
@Configuration//SpringBean配置类
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;

    /**
     * 设置shiro sessionManager 会话管理 引入RedisSessionDAO
     * @param redisSessionDAO 注入redis会话 为了解决shiro的权限数据和会话信息能保存到redis中，实现会话共享
     * @return
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);

        // other stuff...

        return sessionManager;
    }

    /**
     *  配置shiro 的 安全管理 引入RedisCacheManager
     * @param accountRealm 设置realm
     * @param sessionManager
     * @param redisCacheManager
     * @return
     */
    @Bean
    public SessionsSecurityManager securityManager(AccountRealm accountRealm,
                                                   SessionManager sessionManager,
                                                   RedisCacheManager redisCacheManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        // inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);

        //关闭shiro自带的session 使用jwt凭证作为登录token




        return securityManager;
    }

    @Bean//shiro链式过滤器定义
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "jwt"); // 主要通过注解方式@RequiresAuthentication校验权限
        //所有路由都需要经过JwtFilter这个过滤器 判断请求头中是否有jwt信息 有就登陆 否则跳过
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    /**
     *设置工厂方法和 jwtFilter注入shiroFilter
     * @param securityManager 使用第二个方法返回值
     * @param shiroFilterChainDefinition：使用的是上一个方法的返回值 即一个经过设置的shiroFilterChainDefinition
     * @return shiroFilter
     */
    @Bean("shiroFilterFactoryBean")//工厂方法
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition)
    {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        //添加jwt过滤器到Shiro过滤器中
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

}
