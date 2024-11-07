package com.zazhi.service;

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
}
