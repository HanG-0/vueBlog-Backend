package com.vueblog.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.service.BlogService;
import com.vueblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Generator@Hang
 * @since 2024-01-18
 */
@RestController//注解这是个RESTful风格的控制器
public class BlogController {

    @Autowired
    BlogService blogService;

    /**
     * @GetMapping注解是Spring Boot中最常用的注解之一，映射get请求
     * 它可以帮助开发者定义和处理HTTP GET请求。使用@GetMapping注解时，
     * 开发者需要指定URL路径
     *
     * @RequestParam注解表示请求参数“currentPage”的值将被注入到方法参数中。参数并不在路径中
     * 当客户端发送HTTP GET请求“/blog/blogs?currentPage=1”时，
     * Spring Boot会自动将请求映射到list方法，
     * 并将参数“1”注入到方法参数中，
     *可以发现路径中包含了"?"，问号后代表请求需要附带的参数，若有多个以"&"隔开，
     * 请注意和上一种的区别。GET请求和POST请求都可以用。
     *
     * 规律：凡是子类及带有方法或属性的类都要加上注册Bean到Spring IoC的注解；
     */


    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        //page 来源于mybatis-plus
        Page page = new Page(currentPage, 5);        //在数据库中查询page
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    /**
     * PathVariable注解 是另一种传参方式（读出附带在请求中的参数）
     * 将{id}中名为id中的内容传递给id变量
     *
     */
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        //在数据库中查询对应的博客
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }

    @RequiresAuthentication//需要认证
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");
        Blog temp = null;
        if(blog.getId() != null) {//id不为空 是编辑状态
            //从数据库中把记录查出来
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {//添加状态 temp是新的blog

            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        //将blog内容复制到temp 忽略一些参数
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        //将blog数据库更新为temp
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }


}
