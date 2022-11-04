package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetMealMapper extends BaseMapper<Setmeal> {
    public void setSetMealAndCheckGroup(@Param("param") SetMealRelationParam param);

    public void deleteCheckGroupBySetMealId(Integer id);

    public List<CheckGroup> selectCheckGroupBySetMealId(Integer id);

    public List<Map<String, Object>> findSetmealCount();

    public  List<Map<String, Object>> findTop2Setmeal();
}
