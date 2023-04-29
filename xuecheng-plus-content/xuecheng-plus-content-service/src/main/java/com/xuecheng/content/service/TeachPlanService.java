package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import javax.annotation.Resource;
import java.util.List;

public interface TeachPlanService {

    List<TeachplanDto> getTeachPlanInfo(Long courseId, int grade);

    void saveTeachPlan(SaveTeachplanDto saveTeachplanDto);
}
