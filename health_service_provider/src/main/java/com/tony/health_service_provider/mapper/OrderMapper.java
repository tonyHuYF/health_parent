package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
