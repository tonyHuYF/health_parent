package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.CheckItem;
import com.tony.health_interface.service.CheckItemService;
import com.tony.health_service_provider.mapper.CheckItemMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 检查项服务
 */
@DubboService(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Resource
    private CheckItemMapper checkItemMapper;

    /**
     * 新增
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemMapper.insert(checkItem);
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        LambdaQueryWrapper<CheckItem> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(queryPageBean.getQueryString())) {
            wrapper.eq(CheckItem::getCode, queryPageBean.getQueryString());
            wrapper.or().like(CheckItem::getName, queryPageBean.getQueryString());
        }

        Page<CheckItem> page = checkItemMapper.selectPage(
                new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()), wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }
}
