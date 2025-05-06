package com.zazhi.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String avatarUrl;

    private String cfUsername;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
