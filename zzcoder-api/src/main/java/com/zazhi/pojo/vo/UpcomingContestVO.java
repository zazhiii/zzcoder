package com.zazhi.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author lixh
 * @since 2025/8/16 11:53
 */
@Data
public class UpcomingContestVO {
    private String event;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer duration;
    private String href;
    private String resource;
}
