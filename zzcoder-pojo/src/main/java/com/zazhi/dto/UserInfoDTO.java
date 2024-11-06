package com.zazhi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zazhi
 * @date 2024/10/31
 * @description: 用户基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private Long id;

    private String username;

    private String email;

//    private String phoneNumber;

    private String avatarUrl;

    private String cfUsername;

    private Integer status;
}