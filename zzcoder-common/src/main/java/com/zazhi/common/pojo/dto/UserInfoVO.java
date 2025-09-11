package com.zazhi.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long id;

    private String username;

    private String email;

    private String avatarUrl;

    private String cfUsername;

    private Set<String> roles;

    private Set<String> permissions;

    private Timestamp createTime;
}
