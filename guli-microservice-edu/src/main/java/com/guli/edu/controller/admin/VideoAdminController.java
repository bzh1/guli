package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.form.VideoInfoForm;
import com.guli.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "课时管理")
@CrossOrigin
@RestController
@RequestMapping("admin/edu/video")
public class VideoAdminController {
    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课时")
    @PostMapping("save-video-info")
    public R saveVideoInfo(
            @ApiParam(name = "video", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm
            ) {
        videoService.saveVideoInfo(videoInfoForm);

        return R.ok();
    }

    @ApiOperation("更新课时")
    @PutMapping("update-video-info/{id}")
    public R updateVideoById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id,
            @ApiParam(name = "video", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm
    ) {

        videoService.updateVideoById(videoInfoForm);
        return R.ok().message("更新成功");
    }

    @ApiOperation("根据ID查询课时")
    @GetMapping("video-info/{id}")
    public R getVideoById(
            @ApiParam(name = "id", value = "课时id", required = true)
            @PathVariable String id
    ) {

        VideoInfoForm videoInfoForm=videoService.getVideoById(id);

        return R.ok().data("item", videoInfoForm);
    }

    @ApiOperation("根据id删除课时")
    @DeleteMapping("{id}")
    public R removeVideoById(
            @ApiParam(name = "id", value = "课时id", required = true)
            @PathVariable String id
    ) {

        videoService.deleteById(id);

        return R.ok().message("删除成功");
    }
}
