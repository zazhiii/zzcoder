package com.zazhi.service.impl;

import com.zazhi.pojo.entity.ProblemTag;
import com.zazhi.mapper.TagMapper;
import com.zazhi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zazhi
 * @date 2024/11/7
 * @description: 题目标签相关业务
 */

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagMapper tagMapper;

    /**
     * 新增标签
     * @param name
     */
    public void insert(String name) {
        tagMapper.insert(name);
    }

    /**
     * 根据id删除题目标签
     * @param id
     */
    public void deletById(Integer id) {
       tagMapper.deleteById(id);
    }


    /**
     * 获取所有标签
     */
    public List<ProblemTag> list() {
        return tagMapper.list();
    }
}
