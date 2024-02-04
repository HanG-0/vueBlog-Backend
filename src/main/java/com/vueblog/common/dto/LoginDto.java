package com.vueblog.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * ClassName: LoginDto
 * Package: com.vueblog.common.dto
 * Description: Data Transfer Object 将数据封装为普通的javaBeans 用于在各个层之间传输数据
 *
 * @Author lisiyi
 * @Creat 2024/1/20 23:36
 */


@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
