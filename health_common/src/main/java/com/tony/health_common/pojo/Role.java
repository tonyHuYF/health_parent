package com.tony.health_common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 角色
 */
@Data
@TableName("t_role")
public class Role implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name; // 角色名称
    private String keyword; // 角色关键字，用于权限控制
    private String description; // 描述
    @TableField(exist = false)
    private Set<User> users = new HashSet<User>(0);
    @TableField(exist = false)
    private Set<Permission> permissions = new HashSet<Permission>(0);
    @TableField(exist = false)
    private LinkedHashSet<Menu> menus = new LinkedHashSet<Menu>(0);

}
