package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.Teacher;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
@Api(description = "讲师模块")
@CrossOrigin
@RestController
@RequestMapping("/edu/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;

    @ApiOperation("讲师列表")
    @GetMapping
    public R list() {

        List<Teacher> teachers = teacherService.list(null);

        return R.ok().data("items", teachers);
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("{teacherId}")
    public R getById(
            @ApiParam(name = "teacherId", value = "讲师id", required = true)
            @PathVariable String teacherId
    ) {
        //根据id查询讲师信息
        Teacher teacher = teacherService.getById(teacherId);

        //根据讲师id查询讲师的课程列表
       List<Course> courseList= courseService.selectByTeacherId(teacherId);

        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }

    @ApiOperation("分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit
    ) {
        Page<Teacher> teacherPage = new Page<>(page, limit);

        Map<String,Object> map=teacherService.pageListWeb(teacherPage);

        return R.ok().data(map);
    }

}

