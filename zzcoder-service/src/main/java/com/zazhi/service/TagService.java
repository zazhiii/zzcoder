package com.zazhi.service;

import com.zazhi.entity.ProblemTag;

import java.util.List;

public interface TagService {
    /**
     * 新增标签
     * @param name
     */
    void insert(String name);

    /**
     * 根据id删除题目标签
     * @param id
     */
    void deletById(Integer id);

    /**
     * 获取所有标签
     */
    List<ProblemTag> list();

}
