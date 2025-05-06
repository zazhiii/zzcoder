package com.zazhi.pojo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<E> implements Serializable {

    private long total; //总记录数

    private List<E> records; //当前页数据集合

}
