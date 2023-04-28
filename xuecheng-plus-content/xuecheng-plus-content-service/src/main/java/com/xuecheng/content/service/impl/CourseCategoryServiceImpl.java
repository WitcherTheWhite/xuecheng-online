package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String parentId) {
        List<CourseCategoryTreeDto> list = new ArrayList<>();
        LambdaQueryWrapper<CourseCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCategory::getParentid, parentId);
        List<CourseCategory> courseCategories = courseCategoryMapper.selectList(queryWrapper);
        for (CourseCategory courseCategory : courseCategories) {
            CourseCategoryTreeDto treeDto = new CourseCategoryTreeDto();
            BeanUtils.copyProperties(courseCategory, treeDto);
            treeDto.setChildrenTreeNodes(queryTreeNodes((treeDto.getId())));
            list.add(treeDto);
        }

        if (list.size() == 0) {
            return null;
        }
        return list;
    }
}
