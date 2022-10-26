package com.tony.health_interface.service;

import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.CheckItem;

public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult findPage(QueryPageBean queryPageBean);
}
