package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.vod.service.VideoService;
import com.guli.vod.util.AliyunVodSDKUtils;
import com.guli.vod.util.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class VideoServiceImpl implements VideoService {

    @Override
    public String uploadVideo(MultipartFile file) {

        InputStream inputStream = null;
        try {

            inputStream = file.getInputStream();

            String fileName = file.getOriginalFilename();

            String title = fileName.substring(0, fileName.lastIndexOf("."));

            //配置上传请求
            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET,
                    title,
                    fileName,
                    inputStream);

            //执行上传得到响应
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = response.getVideoId();

            if (!response.isSuccess()) {

                if (StringUtils.isEmpty(videoId)) {
                    throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
                }
            }

            return videoId;

        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
    }

    @Override
    public void deleteVideoById(String videoId) {

        DefaultAcsClient client = null;

        try {
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            request.setVideoIds(videoId);

            DeleteVideoResponse response = client.getAcsResponse(request);

        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @Override
    public CreateUploadVideoResponse getUploadAuthAndAddress(String title, String fileName) {

        DefaultAcsClient client = null;
        try {
            //初始化
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //获取请求对象
            CreateUploadVideoRequest request = new CreateUploadVideoRequest();
            request.setFileName(fileName);
            request.setTitle(title);

            //获取响应
            CreateUploadVideoResponse response = client.getAcsResponse(request);

            return response;

        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FETCH_VIDEO_UPLOAD_PLAYAUTH_ERROR);
        }
    }

    @Override
    public RefreshUploadVideoResponse refreshUploadAuthAndAddress(String videoId) {
        DefaultAcsClient client = null;
        try {
            //初始化
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //获取请求对象
            RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
            request.setVideoId(videoId);

            //获取响应
            RefreshUploadVideoResponse response = client.getAcsResponse(request);

            return response;

        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.REFRESH_VIDEO_UPLOAD_PLAYAUTH_ERROR);
        }
    }
}
