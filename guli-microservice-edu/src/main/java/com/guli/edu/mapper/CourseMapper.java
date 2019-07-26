package com.guli.edu.mapper;

import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.edu.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
public interface CourseMapper extends BaseMapper<Course> {

    //根据课程id查询课程发布信息
    CoursePublishVo selectCoursePublishVoById(String id);
}
