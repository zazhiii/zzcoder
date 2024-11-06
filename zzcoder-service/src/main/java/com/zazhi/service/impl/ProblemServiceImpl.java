package com.zazhi.service.impl;

import com.zazhi.dto.ProblemDTO;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.service.ProblemService;
import com.zazhi.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 题目相关业务
 */
@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    ProblemMapper problemMapper;

    /**
     * 添加题目
     *
     * @param problemDTO
     */
    public void addProblem(ProblemDTO problemDTO) {
        Long id = ThreadLocalUtil.getCurrentId();
        problemDTO.setCreateUser(id);
        problemMapper.insert(problemDTO);
    }
}
