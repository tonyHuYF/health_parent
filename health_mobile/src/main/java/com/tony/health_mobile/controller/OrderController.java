package com.tony.health_mobile.controller;

import cn.hutool.core.util.ObjectUtil;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.constant.RedisMessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.Order;
import com.tony.health_common.untils.SMSUtils;
import com.tony.health_interface.service.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @DubboReference
    private OrderService orderService;

    /**
     * 提交订单
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //获取redis验证码
        String code = stringRedisTemplate.opsForValue().get(map.get("telephone").toString() + RedisMessageConstant.SENDTYPE_ORDER);
        if (ObjectUtil.isNotEmpty(map.get("validateCode")) && ObjectUtil.isNotEmpty(code)
                && code.equals(map.get("validateCode").toString())) {
            //验证成功
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                try {
                    //预约成功，发短信
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_CODE, map.get("telephone").toString()
                            , new String[]{map.get("orderDate").toString()});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return result;

        } else {
            //验证失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    /**
     * 根据Id查询订单
     */
    @PostMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            //调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
