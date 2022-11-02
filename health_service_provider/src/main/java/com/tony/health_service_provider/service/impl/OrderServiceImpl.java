package com.tony.health_service_provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.Member;
import com.tony.health_common.pojo.Order;
import com.tony.health_common.pojo.OrderSetting;
import com.tony.health_interface.service.OrderService;
import com.tony.health_service_provider.mapper.MemberMapper;
import com.tony.health_service_provider.mapper.OrderMapper;
import com.tony.health_service_provider.mapper.OrderSettingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@DubboService(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderSettingMapper orderSettingMapper;
    @Resource
    private MemberMapper memberMapper;

    /**
     * 新增
     */
    @Override
    public Result order(Map map) {
        Order order = BeanUtil.fillBeanWithMap(map, new Order(), false);

        //检查是否已经设置的预约设置
        OrderSetting orderSetting = orderSettingMapper.selectOne(new LambdaQueryWrapper<OrderSetting>()
                .eq(OrderSetting::getOrderDate, order.getOrderDate()));

        if (ObjectUtil.isEmpty(orderSetting)) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查是否预约人数已满
        int reservations = orderSetting.getReservations();
        int number = orderSetting.getNumber();
        if ((reservations >= number)) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //判断同一用户是否存在重复预约,如果不是会员，则自动注册
        Member member = memberMapper.selectOne(new LambdaQueryWrapper<Member>()
                .eq(Member::getPhoneNumber, map.get("telephone").toString()));

        if (ObjectUtil.isNotEmpty(member)) {
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderDate, order.getOrderDate());
            wrapper.eq(Order::getSetmealId, order.getSetmealId());
            wrapper.eq(Order::getMemberId, member.getId());
            List<Order> orders = orderMapper.selectList(wrapper);

            if (ObjectUtil.isNotEmpty(orders)) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //自动注册会员
            member = new Member();
            member.setName(map.get("name").toString());
            member.setPhoneNumber(map.get("telephone").toString());
            member.setIdCard(map.get("idCard").toString());
            member.setSex(map.get("sex").toString());
            member.setRegTime(new Date());
            memberMapper.insert(member);
        }

        //成功预约，更新当日预约人数
        order.setMemberId(member.getId());
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderMapper.insert(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.updateById(orderSetting);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }
}
