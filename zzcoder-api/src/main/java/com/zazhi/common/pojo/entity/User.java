package com.zazhi.common.pojo.entity;

import com.zazhi.common.pojo.entity.BaseEntity;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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
