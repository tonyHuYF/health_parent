package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_service_provider.domin.CheckGroupRelationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CheckGroupMapper extends BaseMapper<CheckGroup> {
    public void setCheckGroupAndCheckItem(@Param("param") CheckGroupRelationParam param);

    public List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    public void deleteCheckItemByCheckGroupId(Integer id);

}
