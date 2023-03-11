package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
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

    /**
     * @description 新增课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 课程详细信息
     * @author Starry
     * @date 2023/3/8 17:17
    */
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);

    /**
     * @description TODO
     * @param courseId
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Starry
     * @date 2023/3/8 20:32
    */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);
}
