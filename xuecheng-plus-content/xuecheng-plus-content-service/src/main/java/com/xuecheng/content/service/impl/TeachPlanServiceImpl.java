package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeachPlanServiceImpl implements TeachPlanService {

    @Resource
    private TeachplanMapper teachplanMapper;

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Override
    public List<TeachplanDto> getTeachPlanInfo(Long courseId, int grade) {
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getCourseId, courseId);
        wrapper.eq(Teachplan::getGrade, grade);
        List<Teachplan> teachplanList = teachplanMapper.selectList(wrapper);
        if (teachplanList == null) {
            return null;
        }

        List<TeachplanDto> list = new ArrayList<>();
        for (Teachplan teachplan : teachplanList) {
            TeachplanDto teachplanDto = new TeachplanDto();
            BeanUtils.copyProperties(teachplan, teachplanDto);
            teachplanDto.setTeachPlanTreeNodes(getTeachPlanInfo(courseId, grade + 1));
            list.add(teachplanDto);
        }

        return list;
    }
}
