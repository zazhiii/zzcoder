package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.ProblemSet;
import com.zazhi.mapper.ProblemSetMapper;
import com.zazhi.result.PageResult;
import com.zazhi.service.ProblemSetService;
import com.zazhi.utils.ThreadLocalUtil;
import com.zazhi.vo.ProblemSetVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 修改题单信息
     * @param problemSetDTO
     */
    public void updateProblemSet(ProblemSetDTO problemSetDTO) {
        ProblemSet problemSet = new ProblemSet();
        BeanUtils.copyProperties(problemSetDTO, problemSet);
        Long userId = ThreadLocalUtil.getCurrentId();
        problemSet.setUpdateUser(userId);
        problemSetMapper.updateProblemSet(problemSet);
    }

    /**
     * 分页查询公开题单
     * @param page
     * @param size
     * @param title
     * @return
     */
    public PageResult<ProblemSet> listPublicProblemSet(Integer page, Integer size, String title) {
        PageHelper.startPage(page, size);
        Page<ProblemSet> problemSets = problemSetMapper.listPublicProblemSet(title);
        return new PageResult<>(problemSets.getTotal(), problemSets.getResult());
    }

    /**
     * 查询我的所有题单
     * @return
     */
    public List<ProblemSet> listPrivateProblemSet() {
        return problemSetMapper.listPrivateProblemSet(ThreadLocalUtil.getCurrentId());
    }

    /**
     * 添加题目到题单
     * @param problemSetId
     * @param problemId
     */
    public void addProblemToProblemSet(Integer problemSetId, Integer problemId) {
       problemSetMapper.addProblemToProblemSet(problemSetId, problemId);
    }

    /**
     * 从题单删除题目
     * @param problemSetId
     * @param problemId
     */
    public void deleteProblemFromProblemSet(Integer problemSetId, Integer problemId) {
       problemSetMapper.deleteProblemFromProblemSet(problemSetId, problemId);
    }

    /**
     * 题单详细信息
     * @param id
     * @return
     */
    public ProblemSetVO getProblemSet(Integer id) {
        return problemSetMapper.getProblemSet(id);
    }

    /**
     * 删除题单
     *
     * @param problemSetId
     */
    public void deleteProblemSet(Integer problemSetId) {
        // 题单中有题目则不能删除
        Integer problemCount = problemSetMapper.getProblemCount(problemSetId);
        if (problemCount > 0) {
            throw new RuntimeException("题单中有题目, 不能删除");
        }
        // TODO 只能删除自己的题单
        problemSetMapper.deleteProblemSet(problemSetId);
    }
}
