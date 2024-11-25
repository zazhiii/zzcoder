package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.entity.Problem;
import com.zazhi.entity.TestCase;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.ProblemTagMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.result.PageResult;
import com.zazhi.service.ProblemService;
import com.zazhi.utils.ThreadLocalUtil;
import com.zazhi.vo.ProblemInfoVO;
import com.zazhi.vo.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
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
        Long id = ThreadLocalUtil.getCurrentId();
        problem.setCreateUser(id);
        problem.setUpdateUser(id);
        problemMapper.insert(problem);
    }

    /**
     * 题目条件分页查询
     *
     * @param problemQueryDTO
     * @return
     */
    public PageResult<ProblemVO> page(ProblemQueryDTO problemQueryDTO) {
        PageHelper.startPage(problemQueryDTO.getCurrentPage(), problemQueryDTO.getLimit());
        Page<ProblemVO> res = problemMapper.page(problemQueryDTO);
        return new PageResult<>(res.getTotal(), res.getResult());
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
     * 这里我的设计是通过联查将题目的标签信息和创建人信息一起查出来
     * 这样的好处是只需要一次数据库查询就可以将所有信息查出来
     * 当然也可以分开查，但是会有多次查询
     * 由于查看题目是一个比较频繁的操作，所以这里我选择一次查出来，效率更高
     * 但是sql语句会比较复杂
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


}
