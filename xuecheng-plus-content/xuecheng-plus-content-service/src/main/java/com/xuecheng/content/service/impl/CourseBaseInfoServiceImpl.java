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
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {
        Page<CourseBase> courseBasePage = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
        queryWrapper.like(!StringUtils.isEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        queryWrapper.eq(!StringUtils.isEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);

        return new PageResult<CourseBase>(pageResult.getRecords(), pageResult.getTotal(), pageParams.getPageNo(), pageParams.getPageSize());
    }

    @Transactional
    @Override
    public CourseBaseInfoDto addCourseBase(Long companyId, AddCourseDto addCourseDto) {
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBase);
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBaseMapper.insert(courseBase);

        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);
        saveCourseMarket(courseMarket);

        return getCourseBaseInfo(courseBase.getId());
    }

    public void saveCourseMarket(CourseMarket courseMarket) {
        courseMarketMapper.insert(courseMarket);
    }

    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            return null;
        }

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);

        CourseCategory courseCategoryMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryMt.getName());
        CourseCategory courseCategorySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategorySt.getName());

        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            throw new XueChengPlusException("课程不存在！");
        }
        if (!companyId.equals(courseBase.getCompanyId())) {
            throw new XueChengPlusException("只能修改本机构课程！");
        }
        BeanUtils.copyProperties(editCourseDto, courseBase);
        courseBase.setChangeDate(LocalDateTime.now());
        courseBaseMapper.updateById(courseBase);

        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto, courseMarket);
        courseMarketMapper.updateById(courseMarket);

        return getCourseBaseInfo(courseId);
    }
}
