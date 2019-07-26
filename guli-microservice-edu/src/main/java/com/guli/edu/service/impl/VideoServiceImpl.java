package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.entity.Video;
import com.guli.edu.form.VideoInfoForm;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author bzh
 * @since 2019-07-12
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public void deleteById(String id) {

        //删除视频资源 TODO

        baseMapper.deleteById(id);
    }

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {

        Video video = new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        this.save(video);
    }

    @Override
    public void updateVideoById(VideoInfoForm videoInfoForm) {

        Video video = new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        this.updateById(video);
    }

    @Override
    public VideoInfoForm getVideoById(String id) {

        Video video = this.getById(id);
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(video,videoInfoForm);
        return videoInfoForm;
    }
}
