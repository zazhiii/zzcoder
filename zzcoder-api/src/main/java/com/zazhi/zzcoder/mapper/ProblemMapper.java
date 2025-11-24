package com.zazhi.zzcoder.mapper;

import com.github.pagehelper.Page;
import com.zazhi.zzcoder.common.pojo.dto.ProblemPageDTO;
import com.zazhi.zzcoder.common.pojo.entity.Problem;
import com.zazhi.zzcoder.common.pojo.entity.TestCase;
import com.zazhi.zzcoder.common.pojo.vo.ProblemInfoVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemPageVO;
import com.zazhi.zzcoder.common.pojo.vo.ProblemWithTestCaseVO;
import com.zazhi.zzcoder.common.pojo.vo.TagVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemMapper {
    /**
     * 添加题目
     *
     * @param problem
     */
    @Insert("INSERT INTO `problem` (" +
            "        `problem_number`," +
            "        `title`," +
            "        `time_limit`," +
            "        `memory_limit`," +
            "        `stack_limit`," +
            "        `description`," +
            "        `input_description`," +
            "        `output_description`," +
            "        `source`," +
            "        `difficulty`," +
            "        `hint`," +
            "        `status`," +
            "        `create_user`," +
            "        `update_user`" +
            "        ) VALUES (" +
            "        #{problemId}," +
            "        #{title}," +
            "        #{timeLimit}," +
            "        #{memoryLimit}," +
            "        #{stackLimit}," +
            "        #{description}," +
            "        #{inputDescription}," +
            "        #{outputDescription}," +
            "        #{source}," +
            "        #{difficulty}," +
            "        #{hint}," +
            "        #{status}," +
            "        #{createUser}," +
            "        #{updateUser}" +
            "        )")
    void insert(Problem problem);

    /**
     * 题目条件分页查询
     *
     * @param problemPageDTO
     * @return
     */
    Page<Integer> pageIds(ProblemPageDTO problemPageDTO);

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
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Select("select * from problem where id = #{id}")
    Problem getById(Integer id);

    /**
     * 获取单个题目信息
     *
     * @param id
     * @return
     */
    ProblemInfoVO getProblemInfoById(Integer id);

    /**
     * 获取题目测试数据
     *
     * @param problemId
     * @return
     */
    @Select("select * from test_case where problem_id = #{problemId}")
    List<TestCase> getTestCases(Integer problemId);

    /**
     * 根据题目id查询题目的标签
     * @param problemId 题目id
     * @return 标签列表
     */
    @Select("select t.id, t.name from tag t left join problem_tag pt on t.id = pt.tag_id " +
            "where pt.problem_id = #{problemId}")
    List<TagVO> getProblemTags(Integer problemId);

    /**
     * 根据id列表查询题目分页数据
     * @param result id列表
     * @return 题目分页数据
     */
    List<ProblemPageVO> pageByIds(List<Integer> result);

    /**
     * 获取题目及其测试用例
     * @param problemId
     * @return
     */
    ProblemWithTestCaseVO getProblemWithTestCases(Integer problemId);

    /**
     * 搜索题目
     * @param keyword 关键词
     * @return 题目列表
     */
    @Select("select id, problem_number, title, difficulty from problem " +
            "where title like CONCAT('%', #{keyword}, '%') " +
            "   or problem_number like CONCAT('%', #{keyword}, '%') " +
            "order by id desc limit 50")
    List<ProblemPageVO> searchProblem(String keyword);
}
