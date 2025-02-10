package com.zazhi.mapper;

import com.zazhi.entity.Contest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContestMapper {

    /**
     * 添加比赛
     * @param contest 比赛信息
     */
    @Insert("insert into contest(title, description, start_time, end_time, status, visible, type, password, create_user) values(#{title}, #{description}, #{startTime}, #{endTime}, #{status}, #{visible}, #{type}, #{password}, #{createUser})")
    void insert(Contest contest);
}
