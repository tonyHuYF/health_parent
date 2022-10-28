package com.tony.health_backend.controller;

import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.OrderSetting;
import com.tony.health_common.untils.POIUtils;
import com.tony.health_interface.service.OrderSettingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @DubboReference
    private OrderSettingService orderSettingService;

    /**
     * 文件上传，实现批量导入
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //使用poi解析数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                data.add(new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1])));
            }
            orderSettingService.add(data);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
}
