package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Course;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.query.CourseQuery;
import com.guli.edu.service.CourseService;
import com.guli.edu.vo.CoursePublishVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "课程管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
public class CourseAdminController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程基本信息",name = "CourseInfoForm",required = true)
            @RequestBody CourseInfoForm courseInfoForm){

        String courseId = courseService.saveCourseInfo(courseInfoForm);

        return R.ok().data("courseId",courseId);

    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(value = "课程ID",name = "id",required = true)
            @PathVariable String id){

        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);

        return R.ok().data("item", courseInfoForm);
    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info/{id}")
    public R updateCourseInfoById(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm,
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id
    ){

        courseService.updateCourseInfoById(courseInfoForm);

        return R.ok();
    }

    @ApiOperation("分页课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable("limit")Long limit,
            @ApiParam(name = "courseQuery",value = "查询对象",required = false)
            CourseQuery courseQuery
    ) {

        Page<Course> coursePage = new Page<>(page, limit);

        courseService.pageQuery(coursePage, courseQuery);

        List<Course> records = coursePage.getRecords();

        long total = coursePage.getTotal();

        return R.ok().data("rows", records).data("total", total);


    }

    @ApiOperation("根据ID删除课程")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id",value = "课程ID",required = true)
            @PathVariable String id){

        courseService.removeCourseById(id);

        return R.ok();
    }

    @ApiOperation("根据id获取课程发布信息")
    @GetMapping("course-publish-info/{id}")
    public R getCoursePublishVoById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id
    ) {
        CoursePublishVo coursePublishVo=courseService.getCoursePublishVoById(id);

        return R.ok().data("item", coursePublishVo);
    }

    @ApiOperation("根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id
    ) {

        courseService.publishCourseById(id);

        return R.ok().message("课程发布成功");
    }


}
