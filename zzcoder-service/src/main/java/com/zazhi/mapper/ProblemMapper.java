package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.dto.ProblemDTO;
import com.zazhi.dto.ProblemQueryDTO;
import com.zazhi.entity.Problem;
import com.zazhi.entity.TestCase;
import com.zazhi.vo.ProblemInfoVO;
import com.zazhi.vo.ProblemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemMapper {
    /**
     * 添加题目
     *
     * @param problemDTO
     */
    void insert(ProblemDTO problemDTO);

    /**
     * 题目条件分页查询
     *
     * @param problemQueryDTO
     * @return
     */
    Page<ProblemVO> page(ProblemQueryDTO problemQueryDTO);

    /**
     * 更新题目信息
     *
     * @param problem
     */
    void update(Problem problem);

    /**
     * 根据id删除题目
     *
     * @param pid
     */
    @Delete("delete from problem where id = #{pid}")
    void deleteProblemById(Integer pid);

    /**
     * 获取单个题目信息
     *
     * @param id
     * @return
     */
    @Select("select * from problem where id = #{id}")
    Problem getById(Integer id);

    /**
     * 获取题目测试数据
     *
     * @param problemId
     * @return
     */
    @Select("select * from test_case where problem_id = #{problemId}")
    List<TestCase> getTestCases(Integer problemId);
}
