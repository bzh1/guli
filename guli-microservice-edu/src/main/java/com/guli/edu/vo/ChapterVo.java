package com.guli.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("章节信息")
@Data
public class ChapterVo {
    private String id;

    private String title;

    private List<VideoVo> children = new ArrayList<>();
}
