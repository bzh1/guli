package com.guli.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("课时信息")
@Data
public class VideoVo {

    private String id;

    private String title;

    private Boolean free;

    private String videoSourceId;
}
