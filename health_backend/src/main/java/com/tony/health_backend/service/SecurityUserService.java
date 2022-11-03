package com.tony.health_backend.service;

import com.tony.health_common.pojo.Permission;
import com.tony.health_common.pojo.Role;
import com.tony.health_common.pojo.User;
import com.tony.health_interface.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实现 spring security框架的用户管理
 */
@Service
public class SecurityUserService implements UserDetailsService {
    @DubboReference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();

            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }

            list.add(new SimpleGrantedAuthority(role.getKeyword()));
        }

        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
