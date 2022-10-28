package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.pojo.OrderSetting;
import com.tony.health_interface.service.OrderSettingService;
import com.tony.health_service_provider.mapper.OrderSettingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@DubboService(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Resource
    private OrderSettingMapper orderSettingMapper;

    @Override
    public void add(List<OrderSetting> data) {
        if (ObjectUtil.isNotEmpty(data)) {
            for (OrderSetting setting : data) {
                //判断是否已存在，存在就更新，不存在则插入
                LambdaQueryWrapper<OrderSetting> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderSetting::getOrderDate, setting.getOrderDate());
                Long count = orderSettingMapper.selectCount(wrapper);
                if (count > 0) {
                    //存在，更新操作
                    OrderSetting orderSetting = orderSettingMapper.selectOne(wrapper);
                    orderSetting.setNumber(setting.getNumber());
                    orderSettingMapper.updateById(orderSetting);
                } else {
                    //新增
                    orderSettingMapper.insert(setting);
                }
            }
        }
    }
}
