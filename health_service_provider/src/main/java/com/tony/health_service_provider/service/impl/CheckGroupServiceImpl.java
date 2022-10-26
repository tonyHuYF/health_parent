package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_interface.service.CheckGroupService;
import com.tony.health_service_provider.domin.CheckGroupRelationParam;
import com.tony.health_service_provider.mapper.CheckGroupMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 检查组服务
 */
@DubboService(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Resource
    private CheckGroupMapper checkGroupMapper;

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
}
