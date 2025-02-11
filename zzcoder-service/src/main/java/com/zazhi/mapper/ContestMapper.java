package com.zazhi.mapper;

import com.zazhi.dto.ContestDTO;
import com.zazhi.entity.Contest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestMapper {

    /**
     * 添加比赛
     * @param contest 比赛信息
     */
    @Insert("insert into contest(title, description, start_time, end_time, status, visible, type, password, create_user) values(#{title}, #{description}, #{startTime}, #{endTime}, #{status}, #{visible}, #{type}, #{password}, #{createUser})")
    void insert(Contest contest);

    /**
     * 获取比赛列表
     * @return 比赛列表
     */
    @Select("select * from contest where create_user = #{userId}")
    List<Contest> getContestList(Long userId);

    /**
     * 修改比赛
     * @param contestDTO 比赛信息
     */
    void updateContest(ContestDTO contestDTO);

    /**
     * 删除比赛
     * @param id 比赛id
     */
    @Delete("delete from contest where id = #{id}")
    void deleteContest(Long id);
}
