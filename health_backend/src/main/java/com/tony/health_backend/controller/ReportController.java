package com.tony.health_backend.controller;

import cn.hutool.core.date.DateUtil;
import com.tony.health_common.constant.MessageConstant;
import com.tony.health_common.entity.Result;
import com.tony.health_interface.service.MemberService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @DubboReference
    private MemberService memberService;

    /**
     * 获取会员折线图
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        Map<String, Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        //插入过去一年的日期
        for (int i = 12; i >= 1; i--) {
            months.add(DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -i), new SimpleDateFormat("yyyy-MM")));

        }
        map.put("months", months);

        List<Integer> memberCount = memberService.findMemberCountByMonths(months);

        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);

    }
}
