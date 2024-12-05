package com.zazhi.mapper;

import com.github.pagehelper.Page;
import com.zazhi.dto.ProblemSetDTO;
import com.zazhi.entity.ProblemSet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProblemSetMapper {
    /**
     * 添加题单
     * @param problemSet
     */
    @Insert("insert into problem_set(title, description, status, create_user, update_user) values(#{title}, #{description}, 0, #{createUser}, #{updateUser})")
    void addProblemSet(ProblemSet problemSet);

    /**
     * 修改题单信息
     * @param problemSet
     */
    void updateProblemSet(ProblemSet problemSet);

    /**
     * 分页查询公开题单
     * @param title
     * @return
     */
    Page<ProblemSet> listPublicProblemSet(String title);

    /**
     * 查询我的所有题单
     * @return
     */
    @Select("select * from problem_set where create_user = #{currentId}")
    List<ProblemSet> listPrivateProblemSet(Long currentId);
}
