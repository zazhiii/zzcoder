package com.zazhi.common.pojo.vo;

import lombok.Data;

/**
 *
 * @author lixh
 * @since 2025/8/30 22:04
 */
@Data
public class ProblemSetItemVO {
    private Integer id;
    private String problemId;
    private String title;
    private String difficulty;

    private String externalTitle;
    private String externalDifficulty;
    private String source;
    private String url;
}
