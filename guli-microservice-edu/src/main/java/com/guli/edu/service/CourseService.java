package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.Course;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.query.CourseQuery;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);

    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<Course> coursePage, CourseQuery courseQuery);

    void removeCourseById(String id);

    CoursePublishVo getCoursePublishVoById(String id);

    void publishCourseById(String id);


    Map<String, Object> pageListWeb(Page<Course> coursePage);

    CourseWebVo selectCourseWebVoById(String id);

    List<Course> selectByTeacherId(String teacherId);
}
