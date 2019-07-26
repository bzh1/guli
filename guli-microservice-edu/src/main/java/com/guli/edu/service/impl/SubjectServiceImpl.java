package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.util.ExcelImportUtil;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubjectNestedVo;
import com.guli.edu.vo.SubjectVo;
import com.guli.edu.vo.SubjectVo2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-07-12
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Transactional
    @Override
    public List<String> batchImport(MultipartFile file) throws Exception {

        //错误消息列表
        List<String> errorMsg = new ArrayList<>();
        //创建工具类对象
        ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());
        //获取工作表
        HSSFSheet sheet = excelHSSFUtil.getSheet();

        int rowCount = sheet.getPhysicalNumberOfRows();

        if (rowCount <= 1) {
            errorMsg.add("请填写数据");
            return errorMsg;
        }
        for (int rowNum = 1; rowNum < rowCount; rowNum++) {
            Row rowData = sheet.getRow(rowNum);
            if (rowData != null) {
                //获取一级分类
                Cell levelOneCell = rowData.getCell(0);
                String levelOneValue = "";
                if (levelOneCell != null) {
                    levelOneValue = excelHSSFUtil.getCellValue(levelOneCell).trim();
                    if (StringUtils.isEmpty(levelOneValue)) {
                        errorMsg.add("第" + rowNum + "行一级分类为空");
                        continue;
                    }
                }

                // 判断一级分类是否重复
                Subject subject = this.getByTitle(levelOneValue);
                String parentId = null;
                if (subject == null) {
                    //将一级分类存入数据库
                    Subject subjectLevelOne = new Subject();
                    subjectLevelOne.setSort(rowNum);
                    subjectLevelOne.setTitle(levelOneValue);

                    baseMapper.insert(subjectLevelOne);
                    parentId = subjectLevelOne.getId();

                } else {
                    parentId = subject.getId();
                }

                //获取二级分类
                Cell levelTwoCell = rowData.getCell(1);
                String levelTwoValue = "";
                if (levelTwoCell != null) {
                    levelTwoValue = excelHSSFUtil.getCellValue(levelTwoCell).trim();

                    if (StringUtils.isEmpty(levelTwoValue)) {
                        errorMsg.add("第" + rowNum + "行二级分类为空");
                        continue;
                    }
                }

                //判断二级分类是否重复
                Subject subjectSub = this.getSubByTitle(levelTwoValue, parentId);
                Subject subjectLevelTwo = null;
                if (subjectSub == null) {
                    //将二级分类存入数据库
                    subjectLevelTwo = new Subject();
                    subjectLevelTwo.setTitle(levelTwoValue);
                    subjectLevelTwo.setSort(rowNum);
                    subjectLevelTwo.setParentId(parentId);
                    baseMapper.insert(subjectLevelTwo);
                }
            }
        }
        return errorMsg;
    }

    @Override
    public List<SubjectNestedVo> nestedList() {
        //最终的返回数据
        ArrayList<SubjectNestedVo> subjectNestedVoArrayList = new ArrayList<>();
        //获取一级分类数据记录
        QueryWrapper<Subject> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", 0);
        queryWrapper1.orderByAsc("sort", "id");
        List<Subject> subjects = baseMapper.selectList(queryWrapper1);

        //获取二级分类数据记录
        QueryWrapper<Subject> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id", 0);
        queryWrapper2.orderByAsc("sort", "id");
        List<Subject> subSubjects = baseMapper.selectList(queryWrapper2);

        //填充一级分类vo数据
        int count = subjects.size();
        for (int i = 0; i < count; i++) {
            Subject subject = subjects.get(i);

            //创建一级类别vo对象
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            BeanUtils.copyProperties(subject, subjectNestedVo);
            subjectNestedVoArrayList.add(subjectNestedVo);

            //填充二级类别vo数据
            ArrayList<SubjectVo> subjectVoArrayList = new ArrayList<>();
            int count2 = subSubjects.size();
            for (int j = 0; j < count2; j++) {
                Subject subSubject = subSubjects.get(j);

                if (subject.getId().equals(subSubject.getParentId())) {

                    //创建二级类别vo对象
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subSubject, subjectVo);
                    subjectVoArrayList.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(subjectVoArrayList);
        }
        return subjectNestedVoArrayList;
    }

    @Override
    public List<SubjectVo2> nestedList2() {
        return baseMapper.selectNestedListByParentId("0");
    }

    /**
     * 根据分类名称查询一级分类是否存在
     */
    private Subject getByTitle(String title) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父ID判断二级分类是否存在
     *
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);

        return baseMapper.selectOne(queryWrapper);
    }
}
