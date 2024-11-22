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
