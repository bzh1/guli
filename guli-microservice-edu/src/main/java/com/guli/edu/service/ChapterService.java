package com.guli.edu.service;

import com.guli.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
public interface ChapterService extends IService<Chapter> {

    void deleteChapterById(String id);

    List<ChapterVo> getNestedListByCourseId(String courseId);


}
