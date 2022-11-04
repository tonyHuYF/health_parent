package com.tony.health_service_provider.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.pojo.Member;
import com.tony.health_common.untils.MD5Utils;
import com.tony.health_interface.service.MemberService;
import com.tony.health_service_provider.mapper.MemberMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;

    /**
     * 根据手机号查用户
     */
    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberMapper.selectOne(new LambdaQueryWrapper<Member>().eq(Member::getPhoneNumber, telephone));
        return member;
    }

    /**
     * 新增
     */
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (ObjectUtil.isNotEmpty(password)) {
            String md5Pwd = MD5Utils.md5(password);
            member.setPassword(md5Pwd);
        }
        memberMapper.insert(member);
    }

    /**
     * 根据月份查询会员数据
     */
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        months.forEach(p -> {
            //先补全当月的最后一天
            p += "-01";
            DateTime dateTime = DateUtil.endOfMonth(DateUtil.parseDate(p));
            Long count = memberMapper.selectCount(new LambdaQueryWrapper<Member>().le(Member::getRegTime, dateTime));
            memberCount.add(count.intValue());
        });
        return memberCount;
    }
}
