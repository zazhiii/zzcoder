package com.zazhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zazhi.dto.ProblemDTO;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.entity.Problem;
import com.zazhi.entity.ProblemTag;
import com.zazhi.entity.User;
import com.zazhi.mapper.ProblemMapper;
import com.zazhi.mapper.ProblemTagMapper;
import com.zazhi.mapper.UserMapper;
import com.zazhi.result.PageResult;
import com.zazhi.service.ProblemService;
import com.zazhi.utils.ThreadLocalUtil;
import com.zazhi.vo.ProblemInfoVO;
import com.zazhi.vo.ProblemVO;
import org.springframework.beans.BeanUtils;
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
     * @param problemDTO
     */
    public void addProblem(ProblemDTO problemDTO) {
        Long id = ThreadLocalUtil.getCurrentId();
        problemDTO.setCreateUser(id);
        problemDTO.setUpdateUser(id);
        List<Integer> tagIds = problemDTO.getTagIds();
        problemMapper.insert(problemDTO);
        for (Integer tagId : tagIds){
            problemTagMapper.addTagToProblem(tagId, problemDTO.getId());
        }
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
        for (ProblemVO problemVO : res) { // 查询出每个题目的标签
            List<ProblemTag> tags = problemTagMapper.getTagByProblemId(problemVO.getId());
            problemVO.setTags(tags);
        }
        return new PageResult<>(res.getTotal(), res.getResult());
    }

    /**
     * 更新题目信息
     *
     * @param problemDTO
     */
    public void update(ProblemDTO problemDTO) {
        Problem problem = new Problem();
        problem.setUpdateUser(ThreadLocalUtil.getCurrentId()); // 更新人为当前用户
        BeanUtils.copyProperties(problemDTO, problem);
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
     * @param id
     * @return
     */
    public ProblemInfoVO getProblemInfo(Integer id) {
        ProblemInfoVO problemInfoVO = new ProblemInfoVO();
        // 基本信息
        Problem problem = problemMapper.getById(id);
        BeanUtils.copyProperties(problem, problemInfoVO);
        // 标签信息
        List<ProblemTag> tags = problemTagMapper.getTagByProblemId(id);
        problemInfoVO.setTags(tags);
        // 创建人信息
        User createUser = userMapper.findById(problem.getCreateUser());
        if(createUser != null){
            problemInfoVO.setCreateUser(createUser.getUsername());
        }
        return problemInfoVO;
    }

    /**
     * 添加标签到题目
     * @param problemId
     * @param tagIds
     */
    public void addTagToProblem(Integer problemId, List<Integer> tagIds) {
        for(Integer tagId : tagIds){
            problemTagMapper.addTagToProblem(tagId, problemId);
        }
    }

    /**
     * 删除题目上的标签
     * @param problemId
     * @param tagId
     */
    public void deleteTagFromProblem(Integer problemId, Integer tagId) {
        problemTagMapper.deleteTagFromProblem(problemId, tagId);
    }


}
