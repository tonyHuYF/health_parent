package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetMealMapper extends BaseMapper<Setmeal> {
    public void setSetMealAndCheckGroup(@Param("param") SetMealRelationParam param);

    public void deleteCheckGroupBySetMealId(Integer id);

    public List<CheckGroup> selectCheckGroupBySetMealId(Integer id);
}
