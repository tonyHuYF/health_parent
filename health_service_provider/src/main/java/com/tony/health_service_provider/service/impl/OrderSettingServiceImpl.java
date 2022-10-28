package com.tony.health_service_provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.pojo.OrderSetting;
import com.tony.health_common.vo.OrderSettingVo;
import com.tony.health_interface.service.OrderSettingService;
import com.tony.health_service_provider.mapper.OrderSettingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    /**
     * 根据月份获取预约数据，date格式为 yyyy-MM
     */
    @Override
    public List<OrderSettingVo> getOrderSettingByMonth(String date) {
        List<OrderSettingVo> result = new ArrayList<>();

        if (ObjectUtil.isNotEmpty(date)) {
            String[] split = date.split("-");
            //重构入参，将 2022-1 改为 2022-01
            String fixDate = split[0] + "-" + String.format("%02d", Integer.parseInt(split[1]));

            LambdaQueryWrapper<OrderSetting> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(OrderSetting::getOrderDate, fixDate);

            List<OrderSetting> orderSettings = orderSettingMapper.selectList(wrapper);
            orderSettings.forEach(p -> {
                OrderSettingVo vo = new OrderSettingVo();
                BeanUtil.copyProperties(p, vo);
                //截取日期
                int day = DateUtil.dayOfMonth(p.getOrderDate());
                vo.setDate(day);
                result.add(vo);
            });
        }
        return result;
    }
}
