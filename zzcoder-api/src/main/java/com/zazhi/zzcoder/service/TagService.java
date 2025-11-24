package com.zazhi.zzcoder.service;

import com.zazhi.zzcoder.common.pojo.entity.ProblemTag;

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
