package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tony.health_common.constant.RedisConstant;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_interface.service.SetMealService;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import com.tony.health_service_provider.mapper.SetMealMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@DubboService(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Resource
    private SetMealMapper setMealMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        //每次上传都将fileName放入redis的 set setmealPicDbResource 中
        stringRedisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB_RESOURCE, setmeal.getImg());
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(queryPageBean.getQueryString())) {
            wrapper.like(Setmeal::getCode, queryPageBean.getQueryString());
            wrapper.or().like(Setmeal::getName, queryPageBean.getQueryString());
            wrapper.or().like(Setmeal::getHelpCode, queryPageBean.getQueryString());
        }
        Page<Setmeal> page = setMealMapper.selectPage(
                new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()), wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }
}
