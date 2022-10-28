package com.tony.health_interface.service;

import com.tony.health_common.pojo.OrderSetting;
import com.tony.health_common.vo.OrderSettingVo;

import java.util.List;

public interface OrderSettingService {
    public void add(List<OrderSetting> data);

    public List<OrderSettingVo> getOrderSettingByMonth(String date);
}
