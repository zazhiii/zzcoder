package com.zazhi.common.pojo.vo;

import lombok.Data;
import java.sql.Timestamp;


/**
 * @author lixh
 * @since 2025/8/24 17:38
 */
@Data
public class ProblemSetPageVO {
    private Integer id;
    private String title;
    private Integer status;
    private Integer problemCount;
    private Integer createUserId;
    private String createUserName;
    private Timestamp createTime;
}
