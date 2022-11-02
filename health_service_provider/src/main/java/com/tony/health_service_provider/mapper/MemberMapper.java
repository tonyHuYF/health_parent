package com.tony.health_service_provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tony.health_common.pojo.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
