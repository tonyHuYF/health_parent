package com.tony.health_common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSettingVo implements Serializable {
    private Integer date;
    private Integer number;
    private Integer reservations;
}
