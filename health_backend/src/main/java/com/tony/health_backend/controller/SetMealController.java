package com.tony.health_backend.controller;

import cn.hutool.core.lang.UUID;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.constant.RedisConstant;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.entity.Result;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_common.untils.QiniuUtils;
import com.tony.health_interface.service.SetMealService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 体验套餐管理
 */

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @DubboReference
    private SetMealService setMealService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        //获取后缀
        int index = originalFilename.lastIndexOf(".");
        String suff = originalFilename.substring(index);

        String fileName = UUID.randomUUID() + suff;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //每次上传都将fileName放入redis的 set setmealPicResource 中
            stringRedisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_RESOURCE, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setMealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            //调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }

        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setMealService.findPage(queryPageBean);
    }
}
