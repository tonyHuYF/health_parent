package com.tony.health_interface.service;

import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    public PageResult findPage(QueryPageBean queryPageBean);

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    public void deleteById(Integer id);
}
