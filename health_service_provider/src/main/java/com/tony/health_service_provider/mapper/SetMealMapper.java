package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SetMealMapper extends BaseMapper<Setmeal> {
    public void setSetMealAndCheckGroup(@Param("param") SetMealRelationParam param);
}
