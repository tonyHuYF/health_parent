package com.tony.health_interface.service;

import com.tony.health_common.pojo.User;

public interface UserService {
    public User findByUsername(String username);
}
