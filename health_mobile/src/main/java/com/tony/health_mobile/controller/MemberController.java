package com.tony.health_mobile.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.constant.RedisMessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.Member;
import com.tony.health_interface.service.MemberService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 处理会员相关操作
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference
    private MemberService memberService;

    /**
     * 手机快速登录
     */
    @PostMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        //获取前端传过来的validateCode、telephone
        String telephone = map.get("telephone").toString();
        String validateCode = map.get("validateCode").toString();

        //从redis获取验证码
        String redisCode = stringRedisTemplate.opsForValue().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (validateCode != null && redisCode != null && validateCode.equals(redisCode)) {
            //验证码输入成功
            //判断是否会员，不是的话自动注册
            Member member = memberService.findByTelephone(telephone);
            if (ObjectUtil.isEmpty(member)) {
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //向cookie 写入手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);

            //将会员信息保存到redis，30分钟有效期
            String json = JSONUtil.toJsonStr(member);
            stringRedisTemplate.opsForValue().set(telephone, json, 60 * 30, TimeUnit.SECONDS);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);

        } else {
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
