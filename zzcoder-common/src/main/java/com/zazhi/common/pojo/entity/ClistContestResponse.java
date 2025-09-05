package com.zazhi.common.pojo.entity;

import com.zazhi.common.pojo.vo.UpcomingContestVO;
import lombok.Data;

import java.util.List;

/**
 *
 * @author lixh
 * @since 2025/8/16 12:55
 */
@Data
public class ClistContestResponse {
    List<UpcomingContestVO> objects;
}
