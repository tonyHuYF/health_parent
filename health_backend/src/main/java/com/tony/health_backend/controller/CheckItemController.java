package com.tony.health_backend.controller;

import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.CheckItem;
import com.tony.health_interface.service.CheckItemService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @DubboReference
    private CheckItemService checkItemService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            //调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }


    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult page = checkItemService.findPage(queryPageBean);
        return page;
    }
}
