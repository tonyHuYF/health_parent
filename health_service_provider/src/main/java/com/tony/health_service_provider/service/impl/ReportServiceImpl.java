package com.tony.health_service_provider.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.pojo.Member;
import com.tony.health_common.pojo.Order;
import com.tony.health_interface.service.ReportService;
import com.tony.health_service_provider.mapper.MemberMapper;
import com.tony.health_service_provider.mapper.OrderMapper;
import com.tony.health_service_provider.mapper.SetMealMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SetMealMapper setMealMapper;

    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String, Object> data = new HashMap<>();
        //当日新增会员数
        Long todayNewMember = memberMapper.selectCount(new LambdaQueryWrapper<Member>()
                .between(Member::getRegTime, DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date())));
        data.put("todayNewMember", todayNewMember);
        //总会员数
        Long totalMember = memberMapper.selectCount(null);
        data.put("totalMember", totalMember);
        //本周新增会员数
        Long thisWeekNewMember = memberMapper.selectCount(new LambdaQueryWrapper<Member>()
                .between(Member::getRegTime, DateUtil.beginOfDay(DateUtil.beginOfWeek(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfWeek(DateUtil.date()))));
        data.put("thisWeekNewMember", thisWeekNewMember);
        //本月新增会员数
        Long thisMonthNewMember = memberMapper.selectCount(new LambdaQueryWrapper<Member>()
                .between(Member::getRegTime, DateUtil.beginOfDay(DateUtil.beginOfMonth(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.date()))));
        data.put("thisMonthNewMember", thisMonthNewMember);
        //今日预约数
        Long todayOrderNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date())));
        data.put("todayOrderNumber", todayOrderNumber);
        //今日到诊数
        Long todayVisitsNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()))
                .eq(Order::getOrderStatus, "已到诊"));
        data.put("todayVisitsNumber", todayVisitsNumber);
        //本周预约数
        Long thisWeekOrderNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.beginOfWeek(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfWeek(DateUtil.date()))));
        data.put("thisWeekOrderNumber", thisWeekOrderNumber);
        //本周到诊数
        Long thisWeekVisitsNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.beginOfWeek(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfWeek(DateUtil.date())))
                .eq(Order::getOrderStatus, "已到诊"));
        data.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        //本月预约数
        Long thisMonthOrderNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.beginOfMonth(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.date()))));
        data.put("thisMonthOrderNumber", thisMonthOrderNumber);
        //本月到诊数
        Long thisMonthVisitsNumber = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .between(Order::getOrderDate, DateUtil.beginOfDay(DateUtil.beginOfMonth(DateUtil.date())), DateUtil.endOfDay(DateUtil.endOfMonth(DateUtil.date())))
                .eq(Order::getOrderStatus, "已到诊"));
        data.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        //热门套餐
        List<Map<String, Object>> hotSetmeal = setMealMapper.findTop2Setmeal();
        data.put("hotSetmeal", hotSetmeal);
        //今日时间
        data.put("reportDate", DateUtil.formatDate(DateUtil.date()));
        return data;
    }
}
