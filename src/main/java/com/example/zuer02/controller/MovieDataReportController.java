package com.example.zuer02.controller;

import com.example.zuer02.dao.DictInfoDao;
import com.example.zuer02.dao.movie.MovieBasicInfoDao;
import com.example.zuer02.dao.movie.MovieTypeInfoDao;
import com.example.zuer02.entity.DictInfo;
import com.example.zuer02.entity.movieReport.CountInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Hashtable;
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

    @Autowired
    DictInfoDao dictInfoDao;


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

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/getMovieDBScoreCount",method = RequestMethod.POST)
    public Map<String,Object> getMovieDBScoreCount(@RequestBody Map<String,Object> param) throws  Exception{
        Map<String,Object> resultMap=new HashMap<>();

        List<DictInfo> dictInfoList=getDictValueList("report1MovieCountry");
        System.out.println(dictInfoList);
        for(DictInfo dictInfo:dictInfoList){
            List<CountInfo> movieDBScoreCountList =movieBasicInfoDao.getMovieDBScoreCount(dictInfo.getDictValue());
            Map<String,Object>map=new HashMap<>();
            if(movieDBScoreCountList!=null){
                String[] name=new String[movieDBScoreCountList.size()];
                int[] value=new int[movieDBScoreCountList.size()];
                int i=0;
                for(CountInfo countInfo:movieDBScoreCountList){

                    //name[i]=countInfo.getName();
                    value[i]=countInfo.getValue();
                    i++;
                }
                //map.put("name",name);
                map.put("value",value);
            }
            resultMap.put(dictInfo.getDictCode(),map);
        }

       /* List<CountInfo> movieDBScoreCountChinaList =movieBasicInfoDao.getMovieDBScoreCount("中国大陆");
        if(movieDBScoreCountChinaList!=null){
            String[] nameChina=new String[movieDBScoreCountChinaList.size()];
            int[] valueChina=new int[movieDBScoreCountChinaList.size()];
            int i=0;
            for(CountInfo countInfo:movieDBScoreCountChinaList){

                nameChina[i]=countInfo.getName();
                valueChina[i]=countInfo.getValue();
                i++;
            }
            resultMap.put("nameChina",nameChina);
            resultMap.put("valueChina",valueChina);
        }

        List<CountInfo> movieDBScoreCountUsaList =movieBasicInfoDao.getMovieDBScoreCount("美国");
        if(movieDBScoreCountUsaList!=null){
            String[] nameUsa=new String[movieDBScoreCountUsaList.size()];
            int[] valueUsa=new int[movieDBScoreCountUsaList.size()];
            int i=0;
            for(CountInfo countInfo:movieDBScoreCountUsaList){

                nameUsa[i]=countInfo.getName();
                valueUsa[i]=countInfo.getValue();
                i++;
            }
            resultMap.put("nameUsa",nameUsa);
            resultMap.put("valueUsa",valueUsa);
        }

        List<CountInfo> movieDBScoreCountJapanList =movieBasicInfoDao.getMovieDBScoreCount("日本");
        if(movieDBScoreCountJapanList!=null){
            String[] nameJapan=new String[movieDBScoreCountJapanList.size()];
            int[] valueJapan=new int[movieDBScoreCountJapanList.size()];
            int i=0;
            for(CountInfo countInfo:movieDBScoreCountJapanList){

                nameJapan[i]=countInfo.getName();
                valueJapan[i]=countInfo.getValue();
                i++;
            }
            resultMap.put("nameJapan",nameJapan);
            resultMap.put("valueJapan",valueJapan);
        }

        List<CountInfo> movieDBScoreCountKorList =movieBasicInfoDao.getMovieDBScoreCount("韩国");
        if(movieDBScoreCountKorList!=null){
            String[] nameKor=new String[movieDBScoreCountKorList.size()];
            int[] valueKor=new int[movieDBScoreCountKorList.size()];
            int i=0;
            for(CountInfo countInfo:movieDBScoreCountKorList){

                nameKor[i]=countInfo.getName();
                valueKor[i]=countInfo.getValue();
                i++;
            }
            resultMap.put("nameKor",nameKor);
            resultMap.put("valueKor",valueKor);
        }
        //resultMap.put("data",movieDBScoreCountList);*/



        return resultMap;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/getMovieDBScoreDetailInfo",method = RequestMethod.POST)
    public List<MovieBasicInfoDao> getMovieDBScoreDetailInfo(@RequestBody Map<String,Object> param) throws  Exception{

        String countryIndex=((Integer)param.get("countryIndex")).toString();
        String DBScoreIndex=((Integer)param.get("DBScoreIndex")).toString();
        Map<String,Object> map=new HashMap<>();
        String movieCountry=getDictValue("report1MovieCountry",countryIndex);
        map.put("movieCountry",movieCountry);
        String DBScore=getDictValue("report1MovieScoreSection",DBScoreIndex);
        if(DBScore!=null) {
            String DBScoreStart = DBScore.split(",")[0];
            map.put("DBScoreStart", DBScoreStart);
            String DBScoreEnd = DBScore.split(",")[1];
            map.put("DBScoreEnd", DBScoreEnd);
        }
        List<MovieBasicInfoDao> movieBasicInfoDaoList=movieBasicInfoDao.queryMovieInfoByCountryAndScore(map);



        return movieBasicInfoDaoList;
    }




    public String getDictValue(String dictType,String dictCode) throws Exception{

        DictInfo dictInfo=dictInfoDao.queryDictInfoByDictTypeAndDictId(dictType,dictCode);
        if(dictInfo!=null){
            String dictValue=dictInfo.getDictValue();
            return dictValue;
        }else{
            throw new Exception("字典类型["+dictType+"] 字典编码["+dictCode+"] 数据字典获取失败");
        }

    }

    public List<DictInfo> getDictValueList(String dictType) throws Exception{

        List<DictInfo> dictInfoList=dictInfoDao.queryDictInfoByDictType(dictType);
        if(dictInfoList==null){
            throw new Exception("字典类型["+dictType+"] 数据字典获取失败");
        }
        return dictInfoList;

    }
}