package com.vueblog.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 *实体以及实体校验 hibernate-validator
 * 通过设置实体属性上的注解来实现校验
 * 同时要在controller中添加@Validated注解 表示开启对user的校验
 * </p>
 *
 * @author Generator@Hang
 * @since 2024-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
//  首先在实体的属性上添加对应的校验规则
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "昵称不能为空")
    private String username;

    private String avatar;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime lastLogin;

//    private Integer columnName;没用

/**注解
 *@interface Persons {
 *     Person[]  value();
 * }
 * @Repeatable(Persons.class)
 * @interface Person{
 *     String role default "";
 * }
 * @Person(role="artist")
 * @Person(role="coder")
 * @Person(role="PM")
 * public class SuperMan{
 * }
 *通过@interface定义注解
 *注解的属性也叫成员变量。注解只有成员变量，没有方法。
 * 注解的成员变量在注解的定义中以“无形参的方法”形式来声明，
 * 其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。
 *如：int id();即类型为int，名字为id的成员变量。
 * int id() default 0;即类型为int，名字为id，缺省值为0的成员变量。
 * 注解的赋值即为@注解名(成员变量名=值)。 没有属性的注解可以省略括号
 */
}
