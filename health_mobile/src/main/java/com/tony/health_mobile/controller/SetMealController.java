package com.tony.health_mobile.controller;

import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_interface.service.SetMealService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @DubboReference
    private SetMealService setMealService;

    /**
     * 获取所有套餐
     */
    @PostMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setMealService.getAllSetmeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            //调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 获取指定Id套餐详情
     */
    @PostMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            //调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
