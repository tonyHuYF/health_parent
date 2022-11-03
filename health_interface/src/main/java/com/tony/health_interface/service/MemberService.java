package com.tony.health_interface.service;

import com.tony.health_common.pojo.Member;

public interface MemberService {
    public Member findByTelephone(String telephone);
    public void add(Member member);
}
