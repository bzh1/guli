package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.entity.Chapter;
import com.guli.edu.service.ChapterService;
import com.guli.edu.vo.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "课程章节管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterAdminController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("新增章节")
    @PostMapping
    public R saveChapter(
            @ApiParam(name = "章节对象", value = "chapter", required = true)
            @RequestBody Chapter chapter) {

        chapterService.save(chapter);

        return R.ok().message("新增章节成功");
    }

    @ApiOperation("根据ID查询章节")
    @GetMapping("{id}")
    public R getChapterById(
            @ApiParam(name = "id", value = "章节ID", required = true)
            @PathVariable("id") String id
    ) {
        Chapter chapter=chapterService.getById(id);

        return R.ok().data("item", chapter);
    }

    @ApiOperation("根据ID修改章节")
    @PutMapping("{id}")
    public R updateChapterById(
            @ApiParam(name = "chapter",value = "章节基本信息",required = true)
            @RequestBody Chapter chapter,
            @ApiParam(name = "id",value = "章节ID",required = true)
            @PathVariable("id")String id

    ) {
        chapter.setId(id);
        chapterService.updateById(chapter);

        return R.ok().message("修改成功");
    }

    @ApiOperation("根据ID删除章节")
    @DeleteMapping("{id}")
    public R deleteChapterById(
            @ApiParam(name = "id",value = "章节ID",required = true)
            @PathVariable("id")String id
    ){
        chapterService.deleteChapterById(id);

        return R.ok().message("删除成功");

    }

    @ApiOperation("嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public R getNestedListByCourseId(
            @ApiParam(name = "courseId",value = "课程ID",required = true)
            @PathVariable String courseId
    ) {

        List<ChapterVo> chapterVoList= chapterService.getNestedListByCourseId(courseId);

        return R.ok().data("items", chapterVoList);
    }

}
