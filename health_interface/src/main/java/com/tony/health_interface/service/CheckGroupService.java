package com.tony.health_interface.service;

import com.tony.health_common.pojo.CheckGroup;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);
}
