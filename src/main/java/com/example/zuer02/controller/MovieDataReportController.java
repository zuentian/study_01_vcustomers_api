package com.example.zuer02.controller;

import com.example.zuer02.dao.movie.MovieTypeInfoDao;
import com.example.zuer02.entity.movieReport.MovieTypeCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/getMovieTypeCount", method=RequestMethod.GET)
    public Map<String,Object> getMovieTypeCount() throws Exception {

        Map<String,Object> resultMap=new HashMap<>();
        List<MovieTypeCount> movieTypeCountList=movieTypeInfoDao.getMovieTypeCount();
        resultMap.put("data",movieTypeCountList);
        return resultMap;
    }


}
