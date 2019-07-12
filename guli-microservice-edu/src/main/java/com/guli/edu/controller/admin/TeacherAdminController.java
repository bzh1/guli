package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(description = "讲师管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherAdminController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "查询所有讲师")
    @GetMapping
    public R list() {

        List<Teacher> list = teacherService.list(null);
        return R.ok().data("item", list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable("id") String id) {
        teacherService.removeById(id);
        return R.ok();
    }

    /*@ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R listPage(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "limit",value = "每页记录条数",required = true)
            @PathVariable("limit")Long limit) {

        if (page <= 0 || limit <= 0) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        Page<Teacher> teacherPage = new Page<>(page, limit);

        teacherService.page(teacherPage, null);

        List<Teacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        return R.ok().data("rows", records).data("total", total);
    }*/
    /**
     * 根据讲师名称name，讲师头衔level、讲师入驻时间（时间段）查询
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "limit",value = "每页记录条数",required = true)
            @PathVariable("limit")Long limit,
            @ApiParam(name = "teacherQuery",value = "查询对象",required = false)
            TeacherQuery teacherQuery) {

        if (page <= 0 || limit <= 0) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        Page<Teacher> teacherPage = new Page<>(page, limit);

        teacherService.pageQuery(teacherPage, teacherQuery);

        List<Teacher> records = teacherPage.getRecords();
        long total = teacherPage.getTotal();
        return R.ok().data("rows", records).data("total", total);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping
    public R save(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher) {

        teacherService.save(teacher);

        return R.ok().message("添加成功");
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable("id")Integer id) {

        Teacher teacher = teacherService.getById(id);

        return R.ok().data("item", teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable("id")String id,
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher) {

        teacher.setId(id);
        teacherService.updateById(teacher);

        return R.ok();
    }
}
