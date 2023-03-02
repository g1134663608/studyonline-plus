package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author Starry
 * @version 1.0
 * @description TODO
 * @date 2023/3/1 17:48
 */
public interface CourseBaseInfoService {
    /**
     * @description 课程查询
     * @param params 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     * @author Starry
     * @date 2023/3/1 17:53
    */
    public PageResult<CourseBase> QueryCourseParamsList(PageParams params, QueryCourseParamsDto queryCourseParamsDto);
}
