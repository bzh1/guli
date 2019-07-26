package com.guli.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.statistics.client.UcenterClient;
import com.guli.statistics.entity.Daily;
import com.guli.statistics.mapper.DailyMapper;
import com.guli.statistics.service.DailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author bzh
 * @since 2019-07-20
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDay(String day) {
        //删除已经存在的统计对象
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);

        //获取统计信息
        Integer registerNum = (Integer) ucenterClient.registerCount(day).getData().get("countRegister");

        //创建统计对象
        Daily daily = new Daily();
        daily.setRegisterNum(registerNum);
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));
        daily.setDateCalculated(day);

        //插入数据库
        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("date_calculated", type);
        queryWrapper.between("date_calculated", begin, end);

        HashMap<String, Object> map = new HashMap<>();

        ArrayList<Integer> dataList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        map.put("dataList",dataList);
        map.put("dateList", dateList);

        List<Daily> dailyList = baseMapper.selectList(queryWrapper);

        for (int i = 0; i < dailyList.size(); i++) {

            Daily daily = dailyList.get(i);
            dateList.add(daily.getDateCalculated());

            switch (type) {
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
            }
        }
        return map;
    }
}
