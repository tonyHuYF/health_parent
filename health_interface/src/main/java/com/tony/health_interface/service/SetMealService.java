package com.tony.health_interface.service;

import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.Setmeal;

import java.util.List;

public interface SetMealService {
    public void add(Setmeal setmeal, Integer[] checkGroupIds);

    public PageResult findPage(QueryPageBean queryPageBean);

    public void edit(Setmeal setmeal, Integer[] checkGroupIds);

    public void deleteById(Integer id);

    public List<Setmeal> getAllSetmeal();

}
