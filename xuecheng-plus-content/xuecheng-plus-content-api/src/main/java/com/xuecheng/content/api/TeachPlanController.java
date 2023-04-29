package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("新增或修改课程计划")
    @PostMapping("/teachplan")
    public void saveTeachPlan(@RequestBody SaveTeachplanDto saveTeachplanDto) {
        teachPlanService.saveTeachPlan(saveTeachplanDto);
    }
}
