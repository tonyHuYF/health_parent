package com.tony.health_common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单
 */
@Data
@TableName("t_menu")
public class Menu implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name; // 菜单名称
    private String linkUrl; // 访问路径
    private String path;//菜单项所对应的路由路径
    private Integer priority; // 优先级（用于排序）
    private String description; // 描述
    private String icon;//图标
    @TableField(exist = false)
    private Set<Role> roles = new HashSet<Role>(0);//角色集合
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();//子菜单集合
    private Integer parentMenuId;//父菜单id

}
