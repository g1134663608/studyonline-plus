package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author Starry
 * @version 1.0
 * @description 课程分类操作相关的service
 * @date 2023/3/8 16:11
 */
public interface CourseCategroyService {
    /**
     * @description 课程分类查询
     * @param id 根节点id
     * @return 根节点下面所有子节点
     * @author Starry
     * @date 2023/3/8 16:13
    */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
