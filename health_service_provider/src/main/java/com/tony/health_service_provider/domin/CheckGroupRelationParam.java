package com.tony.health_service_provider.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckGroupRelationParam {
    private Integer checkGroupId;
    private Integer checkItemId;
}
