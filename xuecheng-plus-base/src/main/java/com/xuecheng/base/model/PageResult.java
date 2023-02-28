package com.xuecheng.base.model;

import lombok.Data;

import java.util.List;

/**
 * @author Starry
 * @version 1.0
 * @description TODO
 * @date 2023/2/27 17:45
 */
@Data
public class PageResult<T> {
    // 数据列表
    private List<T> items;

    //总记录数
    private long counts;

    //当前页码
    private long page;

    //每页记录数
    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }
}
