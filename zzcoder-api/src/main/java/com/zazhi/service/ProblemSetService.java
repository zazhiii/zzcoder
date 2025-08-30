package com.zazhi.service;

import com.zazhi.common.pojo.dto.ProblemSetDTO;
import com.zazhi.common.pojo.result.PageResult;
import com.zazhi.common.pojo.vo.ProblemSetPageVO;
import com.zazhi.common.pojo.vo.ProblemSetUpdateDTO;
import com.zazhi.common.pojo.vo.ProblemSetVO;

import java.util.List;

public interface ProblemSetService {
    /**
     * 添加题单
     * @param problemSetDTO
     */
    void addProblemSet(ProblemSetDTO problemSetDTO);

    /**
     * 修改题单信息
     * @param problemSetUpdateDTO
     */
    void updateProblemSet(ProblemSetUpdateDTO problemSetUpdateDTO, Integer id);

    /**
     * 分页查询题单
     * @param page
     * @param size
     * @param title
     * @return
     */
    PageResult<ProblemSetPageVO> pageProblemSet(Integer page, Integer size, String title, Integer status);

    /**
     * 添加题目到题单
     * @param problemSetId
     * @param problemId
     */
    void addProblemToProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    void deleteProblemFromProblemSet(Integer problemSetId, Integer problemId);

    /**
     * 题单详细信息
     * @param id
     * @return
     */
    ProblemSetVO getProblemSetDetail(Integer id);

    /**
     * 删除题单
     * @param id 题单ID
     */
    void deleteProblemSet(Integer id);
}
