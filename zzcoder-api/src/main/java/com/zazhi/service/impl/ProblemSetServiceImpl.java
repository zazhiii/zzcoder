package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.common.exception.code.ProblemError;
import com.zazhi.common.exception.code.ProblemSetError;
import com.zazhi.common.exception.model.BizException;
import com.zazhi.common.pojo.dto.ProblemSetDTO;
import com.zazhi.common.pojo.entity.Problem;
import com.zazhi.common.pojo.entity.ProblemSet;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.vo.*;
import com.zazhi.common.utils.ThreadLocalUtil;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.ProblemSetMapper;
import com.zazhi.service.ProblemSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/12/5
 * @description: 题单相关接口
 */
@Service
@RequiredArgsConstructor
public class ProblemSetServiceImpl implements ProblemSetService {
    private final ProblemSetMapper problemSetMapper;

    private final ProblemMapper problemMapper;

    /**
     * 添加题单
     * @param problemSetDTO 添加题单参数
     */
    public void addProblemSet(ProblemSetDTO problemSetDTO) {
        ProblemSet problemSet = ProblemSet.builder()
                .title(problemSetDTO.getTitle())
                .description(problemSetDTO.getDescription())
                .status(problemSetDTO.getStatus())
                .createUser(ThreadLocalUtil.getCurrentId())
                .updateUser(ThreadLocalUtil.getCurrentId())
                .build();
        problemSetMapper.addProblemSet(problemSet);
    }

    /**
     * 修改题单信息
     * @param problemSetUpdateDTO 修改题单参数
     */
    public void updateProblemSet(ProblemSetUpdateDTO problemSetUpdateDTO, Integer id) {
        ProblemSet problemSet = problemSetMapper.getById(id);
        if(problemSet == null){
            throw new RuntimeException("题单不存在");
        }
        if(!problemSet.getCreateUser().equals(ThreadLocalUtil.getCurrentId())){
            throw new RuntimeException("只能修改自己的题单");
        }
        BeanUtils.copyProperties(problemSetUpdateDTO, problemSet);
        Integer userId = ThreadLocalUtil.getCurrentId();
        problemSet.setUpdateUser(userId);
        problemSetMapper.update(problemSet);
    }

    /**
     * 分页查询公开题单
     * @param page
     * @param size
     * @param title
     * @return
     */
    public PageResult<ProblemSetPageVO> pageProblemSet(Integer page, Integer size, String title, Integer status) {
        PageHelper.startPage(page, size);
        Page<ProblemSetPageVO> problemSets = problemSetMapper.pageProblemSet(title, status);
        return new PageResult<>(problemSets.getTotal(), problemSets.getResult());
    }

    /**
     * 添加题目到题单
     * @param problemSetId
     * @param problemId
     */
    public void addInternalProblem(Integer problemSetId, Integer problemId) {
        ProblemSet problemSet = problemSetMapper.getById(problemSetId);
        if (problemSet == null) {
            throw new BizException(ProblemSetError.PROBLEM_SET_NOT_FOUND);
        }
        if (!problemSet.getCreateUser().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BizException(ProblemSetError.NO_PERMISSION, "只能添加题目到自己的题单");
        }
        Problem problem = problemMapper.getById(problemId);
        if (problem == null) {
            throw new BizException(ProblemError.PROBLEM_NOT_FOUND);
        }
        // 题单中已存在该题目
        Integer count = problemSetMapper.countProblemInProblemSet(problemSetId, problemId);
        if (count > 0) {
            throw new BizException(ProblemSetError.PROBLEM_ALREADY_IN_PROBLEM_SET);
        }
        problemSetMapper.addInternalProblem(problemSetId, problemId);
    }

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    public void deleteInternalProblem(Integer problemSetId, Integer problemId) {
        ProblemSet problemSet = problemSetMapper.getById(problemSetId);
        if (problemSet != null && !problemSet.getCreateUser().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BizException(ProblemSetError.NO_PERMISSION, "只能从自己的题单删除题目");
        }
        problemSetMapper.deleteInternalProblem(problemSetId, problemId);
    }

    /**
     * 题单详细信息
     * @param id 题单ID
     * @return 题单详细信息
     */
    public ProblemSetVO getProblemSetDetail(Integer id) {
        ProblemSetVO problemSetVO = new ProblemSetVO();
        ProblemSetInfoVO problemSetInfo = problemSetMapper.getProblemSetInfo(id);
        BeanUtils.copyProperties(problemSetInfo, problemSetVO);
        // 本站题目
        List<ProblemSetItemInternalVO> internalProblems = problemSetMapper.getInternalProblems(id);
        problemSetVO.setInternalProblems(internalProblems);
        // 外部题目
        List<ProblemSetItemExternalVO> externalProblems = problemSetMapper.getExternalProblems(id);
        problemSetVO.setExternalProblems(externalProblems);
        return problemSetVO;
    }

    /**
     * 删除题单
     *
     * @param problemSetId 题单ID
     */
    public void deleteProblemSet(Integer problemSetId) {
        ProblemSet problemSet = problemSetMapper.getById(problemSetId);
        if (problemSet == null) {
            throw new BizException(ProblemSetError.PROBLEM_SET_NOT_FOUND);
        }
        if (!problemSet.getCreateUser().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BizException(ProblemSetError.NO_PERMISSION, "只能删除自己的题单");
        }
        // 题单中有题目则不能删除
        Integer problemCount = problemSetMapper.getProblemCount(problemSetId);
        if (problemCount > 0) {
            throw new BizException(ProblemSetError.DELETE_FAILED, "题单中有题目，不能删除");
        }
        problemSetMapper.deleteProblemSet(problemSetId);
    }

    /**
     * 从题单删除外部题目
     *
     * @param problemSetId 题单ID
     * @param problemId    题目ID
     */
    @Override
    public void deleteExternalProblem(Integer problemSetId, Integer problemId) {
        ProblemSet problemSet = problemSetMapper.getById(problemSetId);
        if (problemSet != null && !problemSet.getCreateUser().equals(ThreadLocalUtil.getCurrentId())) {
            throw new BizException(ProblemSetError.NO_PERMISSION, "只能从自己的题单删除题目");
        }
        problemSetMapper.deleteExternalProblem(problemId);
    }
}
