package com.zazhi.common.pojo.vo;

import lombok.Data;

/**
 * @author lixh
 * @since 2025/8/24 17:38
 */
@Data
public class ProblemSetPageVO {
    private Integer id;
    private String title;
    private Integer status;
    private Integer createUserId;
    private String createUserName;
}
