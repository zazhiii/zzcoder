package com.zazhi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {

    /**
     * 新增标签
     * @param name
     */
    @Insert("insert into tag (name) values (#{name})")
    void insert(String name);

    /**
     * 根据id删除标签
     * @param id
     */
    @Delete("delete from tag where id = #{id}")
    void deleteById(Integer id);
}
