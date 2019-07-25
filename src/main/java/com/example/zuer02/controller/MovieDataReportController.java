package com.example.zuer02.controller;

import com.example.zuer02.dao.movie.MovieBasicInfoDao;
import com.example.zuer02.dao.movie.MovieTypeInfoDao;
import com.example.zuer02.entity.movieReport.CountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//视图解析器
@EnableAutoConfiguration//自动配置
@RequestMapping(value="MovieDataReport")
public class MovieDataReportController {

    @Autowired
    MovieTypeInfoDao movieTypeInfoDao;

    @Autowired
    MovieBasicInfoDao movieBasicInfoDao;


    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/getMovieTypeCount", method = RequestMethod.POST)
    public Map<String, Object> getMovieTypeCount(@RequestBody Map<String, Object> param) throws Exception {//电影类型报告
        Integer selectShowMax = (Integer) param.get("selectShowMax");//饼图先展示最多是几项
        Map<String, Object> resultMap = new HashMap<>();
        List<CountInfo> movieTypeCountList = movieTypeInfoDao.getMovieTypeCount();
        resultMap.put("data", movieTypeCountList);//所有电影各个类型的数量和名字


        int min = 0;//前几项最小的数量
        if (movieTypeCountList != null && selectShowMax > 0) {
            if (movieTypeCountList.size() > selectShowMax) {
                min = movieTypeCountList.get(selectShowMax).getValue();
            } else {
                min = movieTypeCountList.get(movieTypeCountList.size()).getValue();
            }
        }
        Map<String, Boolean> selectShowMap = new HashMap<>();
        for (CountInfo movieTypeCount : movieTypeCountList) {
            if (movieTypeCount.getValue() >= min) {
                selectShowMap.put(movieTypeCount.getName(), true);
            } else {
                selectShowMap.put(movieTypeCount.getName(), false);
            }

        }

        resultMap.put("selectShowMap", selectShowMap);//所有电影各个类型的数量和名字

        return resultMap;
    }


    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/getMovieCountryCount", method = RequestMethod.POST)
    public Map<String, Object> getMovieCountryCount(@RequestBody Map<String, Object> param) throws Exception {//电影地区报告

        Map<String,Object> resultMap=new HashMap<>();
        List<CountInfo> movieTypeCountList = movieBasicInfoDao.getMovieCountryCount();
        resultMap.put("data",movieTypeCountList);
        return resultMap;
    }
}