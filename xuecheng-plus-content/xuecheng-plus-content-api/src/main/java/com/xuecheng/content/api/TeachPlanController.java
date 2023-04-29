package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
public class TeachPlanController {

    @Resource
    private TeachPlanService teachPlanService;

    @ApiOperation("查询课程计划")
    @GetMapping("/teachplan/{id}/tree-nodes")
    public List<TeachplanDto> getTeachPlanInfo(@PathVariable Long id) {
        return teachPlanService.getTeachPlanInfo(id, 1);
    }
}
