package com.zazhi.zzcoder.common.pojo.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable  {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String cfUsername;
    private Integer status;
}
