package com.zazhi.common.pojo.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 *
 * @author lixh
 * @since 2025/9/5 15:15
 */
@Data
public class RoleAndPermissionVO {
    private String userId;
    private Set<String> roles;
    private Set<String> permissions;
}
