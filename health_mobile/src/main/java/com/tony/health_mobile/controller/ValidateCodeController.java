package com.tony.health_mobile.controller;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.constant.RedisMessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.untils.SMSUtils;
import com.tony.health_common.untils.ValidateCodeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 体检预约发送验证码
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        try {
            //发送验证码
            String code = ValidateCodeUtils.generateValidateCode(6) + "";
            String[] param = {code, "5"};
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE_2, telephone, param);

            //保存验证码
            stringRedisTemplate.opsForValue().set(telephone + RedisMessageConstant.SENDTYPE_ORDER, code, 60 * 5, TimeUnit.SECONDS);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
