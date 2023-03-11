package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description TODO
 * @author Starry
 * @version 1.0
 * @date 2023/3/1 17:55
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Override
    public PageResult<CourseBase> QueryCourseParamsList(PageParams params, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //拼接查询条件
        //根据课程名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        //根据课程审核状态
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        //根据课程发布状态
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());
        //分页参数
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());
        //分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);
        //数据
        List<CourseBase> records = pageResult.getRecords();
        //总记录数
        long total = pageResult.getTotal();
        //准备返回数据(List<T> items, long counts, long page, long pageSize)
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(records, total, params.getPageNo(), params.getPageSize());
        return courseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        //参数合法性校验
        if (StringUtils.isBlank(dto.getName())) {
            throw new XueChengPlusException("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            throw new XueChengPlusException("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            throw new XueChengPlusException("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            throw new XueChengPlusException("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            throw new XueChengPlusException("适应人群");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            throw new XueChengPlusException("收费规则为空");
        }
        //向课程基本信息表course_base写入数据
        CourseBase courseBaseNew = new CourseBase();
        //只要属性名一样就可以拷贝对象
        BeanUtils.copyProperties(dto,courseBaseNew);
        courseBaseNew.setCompanyId(companyId);
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //审核状态默认为未提交
        courseBaseNew.setAuditStatus("202002");
        //发布状态默认为未发布
        courseBaseNew.setStatus("203001");
        //插入数据库
        int insert = courseBaseMapper.insert(courseBaseNew);
        if (insert <= 0) {
            throw new RuntimeException("添加课程失败");
        }
        //向课程营销表course_market写入数据
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseBaseNew.getId());
        saveCourseMarket(courseMarketNew);
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseBaseNew.getId());
        return courseBaseInfo;
    }
    //查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if (courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //todo:课程分类名称查出来
//        CourseCategory courseCategory = courseCategoryMapper.selectById(courseBase.getCourseCategoryId());
//        courseBaseInfoDto.setCourseCategoryName(courseCategory.getName());
        return courseBaseInfoDto;
    }
    //单独一个方法保存营销信息
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //合法校验
        String charge = courseMarketNew.getCharge();
        if (StringUtils.isEmpty(charge)) {
            throw new XueChengPlusException("收费规则为空");
        }
        //收费价格没写
        if (charge.equals("201001")) {
            if(courseMarketNew.getPrice()==null || courseMarketNew.getPrice().floatValue()<=0){
                throw new XueChengPlusException("收费价格不能为空且必须大于0");
            }
        }
        //从数据库查询，存在则更新，否则添加
        CourseMarket courseMarket = courseMarketMapper.selectById(courseMarketNew.getId());
        if (courseMarket == null) {
            //插入
            int insert = courseMarketMapper.insert(courseMarketNew);
            return insert;
        } else {
            BeanUtils.copyProperties(courseMarketNew,courseMarket);
            courseMarket.setId(courseMarketNew.getId());
            int i = courseMarketMapper.updateById(courseMarket);
            return i;
        }
    }
}
