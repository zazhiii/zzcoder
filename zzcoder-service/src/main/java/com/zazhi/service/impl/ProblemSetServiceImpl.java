package com.zazhi.service.impl;

import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.Problem;
import com.zazhi.entity.ProblemSet;
import com.zazhi.mapper.ProblemSetMapper;
import com.zazhi.service.ProblemSetService;
import com.zazhi.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单相关接口
 */
@Service
public class ProblemSetServiceImpl implements ProblemSetService {

    @Autowired
    ProblemSetMapper problemSetMapper;

    /**
     * 添加题单
     * @param problemSetDTO
     */
    public void addProblemSet(ProblemSetDTO problemSetDTO) {
        ProblemSet problemSet = new ProblemSet();
        BeanUtils.copyProperties(problemSetDTO, problemSet);
        Long userId = ThreadLocalUtil.getCurrentId();
        problemSet.setCreateUser(userId);
        problemSet.setUpdateUser(userId);
        problemSetMapper.addProblemSet(problemSet);
    }
}
