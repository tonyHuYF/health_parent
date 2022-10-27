package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_interface.service.SetMealService;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import com.tony.health_service_provider.mapper.SetMealMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@DubboService(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Resource
    private SetMealMapper setMealMapper;

    /**
     * 新增
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //新增套餐数据
        setMealMapper.insert(setmeal);
        //插入套餐与检查组关系
        if (ObjectUtil.isNotEmpty(checkGroupIds)) {
            for (Integer checkGroupId : checkGroupIds) {
                setMealMapper.setSetMealAndCheckGroup(new SetMealRelationParam(setmeal.getId(), checkGroupId));
            }
        }

    }
}
