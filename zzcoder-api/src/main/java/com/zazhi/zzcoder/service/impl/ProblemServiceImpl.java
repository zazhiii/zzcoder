package com.zazhi.zzcoder.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.zzcoder.common.pojo.dto.ProblemPageDTO;
import com.zazhi.zzcoder.common.pojo.entity.Problem;
import com.zazhi.zzcoder.common.pojo.entity.TestCase;
import com.zazhi.zzcoder.common.pojo.result.PageResult;
import com.zazhi.zzcoder.common.pojo.vo.ProblemInfoVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemPageVO;
import com.zazhi.zzcoder.common.pojo.vo.TagVO;
import com.zazhi.zzcoder.common.utils.ThreadLocalUtil;
import com.zazhi.zzcoder.mapper.ProblemMapper;
import com.zazhi.zzcoder.mapper.ProblemTagMapper;
import com.zazhi.zzcoder.mapper.UserMapper;
import com.zazhi.zzcoder.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 题目相关业务
 */
@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    ProblemTagMapper problemTagMapper;

    @Autowired
    UserMapper userMapper;
    
    /**
     * 添加题目
     *
     * @param problem
     */
    public void addProblem(Problem problem) {
        Integer id = ThreadLocalUtil.getCurrentId();
        problem.setCreateUser(id);
        problem.setUpdateUser(id);
        problemMapper.insert(problem);
    }

    /**
     * 题目条件分页查询
     * 注意：这里先查满足条件的题目id列表，再根据id列表查询题目信息，避免mybatis结果集映射和分页插件冲突
     *
     * @param problemPageDTO
     * @return
     */
    public PageResult<ProblemPageVO> page(ProblemPageDTO problemPageDTO) {
        PageHelper.startPage(problemPageDTO.getPage(), problemPageDTO.getPageSize());
        Page<Integer> ids = problemMapper.pageIds(problemPageDTO);
        List<ProblemPageVO> records = problemMapper.pageByIds(ids.getResult());
        return new PageResult<>(ids.getTotal(), records);
    }

    /**
     * 更新题目信息
     *
     * @param problem
     */
    public void update(Problem problem) {
        problem.setUpdateUser(ThreadLocalUtil.getCurrentId()); // 更新人为当前用户
        problemMapper.update(problem);
    }

    /**
     * 删除题目
     *
     * @param pid 题目id
     */
    public void deleteProblemWithTags(Integer pid) {
        problemMapper.deleteProblemById(pid);
        problemTagMapper.deleteTagByProblemId(pid);
    }

    /**
     * 获取单个题目信息
     *
     * @param id 题目id
     * @return ProblemInfoVO
     */
    public ProblemInfoVO getProblemInfo(Integer id) {
        return problemMapper.getProblemInfoById(id);
    }

    /**
     * 添加标签到题目
     * @param problemId
     * @param tagId
     */
    public void addTagToProblem(Integer problemId, Integer tagId) {
        problemTagMapper.addTagToProblem(tagId, problemId);
    }

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    public void deleteTagFromProblem(Integer problemId, Integer tagId) {
        problemTagMapper.deleteTagFromProblem(problemId, tagId);
    }

    /**
     * 为题目添加测试用例
     * @param testCase
     */
    public void addTestCase(TestCase testCase) {
       problemTagMapper.addTestCase(testCase);
    }

    /**
     * 删除测试用例
     * @param id
     */
    public void deleteTestCase(Integer id) {
       problemTagMapper.deleteTestCase(id);
    }

    /**
     * 获取题目的测试用例
     * @param problemId
     * @return
     */
    public List<TestCase> getTestCases(Integer problemId) {
        return problemTagMapper.getTestCases(problemId);
    }

    /**
     * 根据题目id查询题目的标签
     * @param problemId 题目id
     * @return 标签列表
     */
    @Override
    public List<TagVO> getProblemTags(Integer problemId) {
        return problemMapper.getProblemTags(problemId);
    }

    /**
     * 搜索题目
     * @param keyword 关键词
     * @return 题目列表
     */
    @Override
    public List<ProblemPageVO> searchProblem(String keyword) {
        return problemMapper.searchProblem(keyword);
    }


}
