package com.tony.health_backend.controller;

import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 获取当前用户的用户名
     */
    @GetMapping("/getUsername")
    public Result getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user != null) {
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
