package com.zazhi.zzcoder.security;

import lombok.Data;

import java.util.Set;

/**
 *
 * @author lixh
 * @since 2025/9/11 16:57
 */
@Data
public class RoleAndPermission {
    private Integer userId;
    private Set<String> roles;
    private Set<String> permissions;
}
