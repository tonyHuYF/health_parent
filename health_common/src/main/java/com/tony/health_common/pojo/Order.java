package com.tony.health_common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检预约信息
 */
@Data
@TableName("t_order")
public class Order implements Serializable {
    @TableField(exist = false)
    public static final String ORDERTYPE_TELEPHONE = "电话预约";
    @TableField(exist = false)
    public static final String ORDERTYPE_WEIXIN = "微信预约";
    @TableField(exist = false)
    public static final String ORDERSTATUS_YES = "已到诊";
    @TableField(exist = false)
    public static final String ORDERSTATUS_NO = "未到诊";
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer memberId;//会员id
    private Date orderDate;//预约日期
    private String orderType;//预约类型 电话预约/微信预约
    private String orderStatus;//预约状态（是否到诊）
    private Integer setmealId;//体检套餐id

    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

    public Order(Integer id, Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.id = id;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmealId = setmealId;
    }

}
