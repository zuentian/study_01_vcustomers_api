package com.example.zuer02.controller;

import com.example.zuer02.dao.DictInfoDao;
import com.example.zuer02.dao.movie.MovieBasicInfoDao;
import com.example.zuer02.dao.movie.MovieTypeInfoDao;
import com.example.zuer02.entity.DictInfo;
import com.example.zuer02.entity.movieReport.CountInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        List<DictInfo> dictInfoListScore=getDictValueList("report1MovieScoreSection");
        String[] movieScoreArray=new String[dictInfoListScore.size()];
        int index1=0;
        for(DictInfo dictInfoScore:dictInfoListScore){
            String value=dictInfoScore.getDictValue();
            if(value!=null){
                String[] values=value.split(",");
                movieScoreArray[index1]=values[0]+"-"+values[1]+"分";
            }
            index1++;
        }
        resultMap.put("movieScoreArray",movieScoreArray);//电影报告中X轴中的分段名字


        List<DictInfo> dictInfoList=getDictValueList("report1MovieCountry");
        //System.out.println(dictInfoList);
        String[] movieCountryArray=new String[dictInfoList.size()];
        List<int[]> movieValueArray=new ArrayList<>();
        int index=0;
        List<CountInfo> listArray=new ArrayList<>();
        for(DictInfo dictInfo:dictInfoList){
            if(dictInfo.getDictValue()==null||"".equals(dictInfo.getDictValue())){
                movieCountryArray[index]="全球电影";
            }else{
                movieCountryArray[index]=dictInfo.getDictValue()+"电影";
            }

            String scoreStart="";
            String scoreEnd="";
            List<CountInfo> list=new ArrayList<>();
            for(DictInfo dictInfoScore:dictInfoListScore){
                scoreStart=(dictInfoScore.getDictValue().split(","))[0];
                scoreEnd=(dictInfoScore.getDictValue().split(","))[1];
                Map<String,Object> paramMap=new HashMap<String, Object>();
                paramMap.put("scoreStart",scoreStart);
                paramMap.put("scoreEnd",scoreEnd);
                paramMap.put("movieCountry",dictInfo.getDictValue());
                List<CountInfo> movieDBScoreCountList =movieBasicInfoDao.getMovieDBScoreCount(paramMap);
                list.addAll(movieDBScoreCountList);

            }
            int[] value=new int[list.size()];
            int i=0;
            for(CountInfo lists:list){
                value[i]=lists.getValue();
                i++;
            }
            movieValueArray.add(value);
            index++;
        }
        resultMap.put("movieValueArray",movieValueArray);//电影报告每个分段的对应的电影部数
        resultMap.put("movieCountryArray",movieCountryArray);//电影报告中出现的电影制片地区
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