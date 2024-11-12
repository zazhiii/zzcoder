package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.dto.JudgeDTO;
import com.zazhi.dto.SubmissionQueryDTO;
import com.zazhi.entity.Submission;
import com.zazhi.mapper.JudgeMapper;
import com.zazhi.result.PageResult;
import com.zazhi.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zazhi
 * @date 2024/11/12
 * @description: 判题模块接口实现
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    JudgeMapper judgeMapper;

    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    public Long submitCode(JudgeDTO judgeDTO) {
        // TODO: 判题模块接口实现
        // 生成提交id
        // 生成判题任务
        // 将判题任务放入消息队列
        // 记录任务状态, 如pending(生成submission存入数据库)
        // 返回提交id
        return null;
    }

    /**
     * 分页条件查询提交记录列表
     * @param submissionQueryDTO
     * @return
     */
    public PageResult<Submission> getSubmissions(SubmissionQueryDTO submissionQueryDTO) {
        PageHelper.startPage(submissionQueryDTO.getCurrentPage(), submissionQueryDTO.getLimit());
        Page<Submission> res = judgeMapper.getSubmissions(submissionQueryDTO);
        return new PageResult<>(res.getTotal(), res.getResult());
    }
}
