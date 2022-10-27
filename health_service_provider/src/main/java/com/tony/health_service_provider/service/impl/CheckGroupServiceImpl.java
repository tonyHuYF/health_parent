package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_interface.service.CheckGroupService;
import com.tony.health_service_provider.domin.CheckGroupRelationParam;
import com.tony.health_service_provider.mapper.CheckGroupMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检查组服务
 */
@DubboService(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Resource
    private CheckGroupMapper checkGroupMapper;

    /**
     * 新增
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //插入检查组
        checkGroupMapper.insert(checkGroup);
        if (ObjectUtil.isNotEmpty(checkitemIds)) {
            for (Integer checkitemId : checkitemIds) {
                //插入检查组与检查项关联关系
                checkGroupMapper.setCheckGroupAndCheckItem(new CheckGroupRelationParam(checkGroup.getId(), checkitemId));
            }
        }
    }

    /**
     * 单条查询
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.selectById(id);
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        LambdaQueryWrapper<CheckGroup> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(queryPageBean.getQueryString())) {
            wrapper.like(CheckGroup::getCode, queryPageBean.getQueryString());
            wrapper.or().like(CheckGroup::getName, queryPageBean.getQueryString());
            wrapper.or().like(CheckGroup::getHelpCode, queryPageBean.getQueryString());
        }
        Page<CheckGroup> page = checkGroupMapper.selectPage(
                new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()), wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 根据检查组查检查项信息
     */
    @Override
    public List<Integer> findCheckItemIdByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdByCheckGroupId(id);
    }
}
