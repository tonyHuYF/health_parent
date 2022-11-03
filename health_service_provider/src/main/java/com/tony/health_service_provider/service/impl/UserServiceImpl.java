package com.tony.health_service_provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tony.health_common.pojo.Menu;
import com.tony.health_common.pojo.Permission;
import com.tony.health_common.pojo.Role;
import com.tony.health_common.pojo.User;
import com.tony.health_interface.service.UserService;
import com.tony.health_service_provider.mapper.MenuMapper;
import com.tony.health_service_provider.mapper.PermissionMapper;
import com.tony.health_service_provider.mapper.RoleMapper;
import com.tony.health_service_provider.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@DubboService(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 根据用户名查询用户
     */
    @Override
    public User findByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user != null) {
            //填充用户角色role
            List<Role> roles = roleMapper.queryRoleByUserId(user.getId());
            if ((roles != null)) {
                for (Role role : roles) {
                    //填充角色对于权限permission
                    List<Permission> permissions = permissionMapper.queryPermissionByRoleId(role.getId());
                    if (permissions != null) {
                        role.setPermissions(new HashSet<>(permissions));
                    }

                    //填充角色对于菜单menu
                    List<Menu> menus = menuMapper.queryMenuByRoleId(role.getId());
                    if (menus != null) {
                        role.setMenus(new LinkedHashSet<>(menus));
                    }
                }

                user.setRoles(new HashSet<>(roles));
            }
        }
        return user;
    }
}
