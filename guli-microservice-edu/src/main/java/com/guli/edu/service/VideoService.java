package com.guli.edu.service;

import com.guli.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.form.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
public interface VideoService extends IService<Video> {

    void deleteById(String id);

    void saveVideoInfo(VideoInfoForm videoInfoForm);

    void updateVideoById(VideoInfoForm videoInfoForm);

    VideoInfoForm getVideoById(String id);
}
