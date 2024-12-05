package com.zazhi.mapper;

import com.zazhi.entity.ProblemSet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemSetMapper {
    /**
     * 添加题单
     * @param problemSet
     */
    @Insert("insert into problem_set(title, description, status, create_user, update_user) values(#{title}, #{description}, 0, #{createUser}, #{updateUser})")
    void addProblemSet(ProblemSet problemSet);
}
