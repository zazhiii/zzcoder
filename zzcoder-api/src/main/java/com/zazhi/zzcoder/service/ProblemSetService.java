package com.zazhi.zzcoder.service;

import com.zazhi.zzcoder.common.pojo.dto.ProblemSetDTO;
import com.zazhi.zzcoder.common.pojo.result.PageResult;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetPageVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetUpdateDTO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemSetVO;

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
     * @param problemSetId 题单ID
     * @param problemId 题目ID
     */
    void addInternalProblem(Integer problemSetId, Integer problemId);

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    void deleteInternalProblem(Integer problemSetId, Integer problemId);

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

    /**
     * 从题单删除外部题目
     * @param problemSetId 题单ID
     * @param problemId 题目ID
     */
    void deleteExternalProblem(Integer problemSetId, Integer problemId);
}
