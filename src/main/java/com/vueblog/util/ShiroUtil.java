package com.vueblog.util;

import com.vueblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * ClassName: ShiroUtil
 * Package: com.vueblog.util
 * Description:
 *
 * @Author lisiyi
 * @Creat 2024/1/20 23:49
 */


public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
