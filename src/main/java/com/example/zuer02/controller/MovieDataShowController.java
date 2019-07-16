package com.example.zuer02.controller;


import com.example.zuer02.dao.movie.*;
import com.example.zuer02.entity.User;
import com.example.zuer02.entity.movie.*;
import com.example.zuer02.utils.DateUtil;
import com.example.zuer02.utils.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController//视图解析器
@EnableAutoConfiguration//自动配置
@RequestMapping(value="MovieDataShow")
public class MovieDataShowController {


    private final String userId="zuer";
    @Autowired
    MovieTypeDao movieTypeDao;

    @Autowired
    MovieBasicInfoDao movieBasicInfoDao;
    @Autowired
    MovieUserInfoDao movieUserInfoDao;
    @Autowired
    MovieTypeInfoDao movieTypeInfoDao;
    @Autowired
    MovieRelNameInfoDao movieRelNameInfoDao;
    @Autowired
    MoviePictureInfoDao moviePictureInfoDao;

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/getMonthMovieCount", method=RequestMethod.GET)
    public String getMonthMovieCount() throws Exception {

        return "success";
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/queryMovieType",method = RequestMethod.POST)
    public List<MovieType> queryMovieType()throws Exception{

        List<MovieType> list=movieTypeDao.queryMovieType();

        return list;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/addMovieData", method=RequestMethod.POST)
    public String uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {


        try{
            //电影基本信息
            MovieBasicInfo movieBasicInfo=new MovieBasicInfo();
            String movieId=UUID.randomUUID().toString();
            movieBasicInfo.setMovieId(movieId);
            movieBasicInfo.setMovieName(request.getParameter("movieName"));
            movieBasicInfo.setMovieEnglishName(request.getParameter("movieEnglishName"));
            movieBasicInfo.setMovieCountry(request.getParameter("movieCountry"));
            movieBasicInfo.setMovieDBScore(request.getParameter("movieDBScore"));
            movieBasicInfo.setMovieShowTime(DateUtil.gmtToStringYMD(request.getParameter("movieShowTime")));
            movieBasicInfo.setMovieContent(request.getParameter("movieContent"));
            //System.out.println(movieBasicInfo);
            movieBasicInfoDao.insertMovieBasicInfo(movieBasicInfo);

            //用户观影信息
            MovieUserInfo movieUserInfo=new MovieUserInfo();
            movieUserInfo.setUserId(userId);//先写死
            movieUserInfo.setMovieId(movieId);//
            movieUserInfo.setMovieIsWatch(request.getParameter("movieIsWatch"));
            movieUserInfo.setMovieWatchTime(DateUtil.gmtToStringYMD(request.getParameter("movieWatchTime")));
            //System.out.println(movieUserInfo);
            movieUserInfoDao.insertMovieUserInfo(movieUserInfo);


            //电影类型
            if(!StringUtils.isEmpty(request.getParameter("movieTypes"))){
                String[] movieTypes= request.getParameter("movieTypes").split(",");
                for (int i = 0; i < movieTypes.length; i++) {
                    MovieTypeInfo movieTypeInfo=new MovieTypeInfo();
                    movieTypeInfo.setMovieTypeId(UUID.randomUUID().toString());
                    movieTypeInfo.setMovieId(movieId);
                    movieTypeInfo.setMovieCode(movieTypes[i]);
                    //System.out.println(movieTypeInfo);
                    movieTypeInfoDao.insertMovieTypeInfo(movieTypeInfo);
                }
            }

            //电影相关人物信息
            if(!StringUtils.isEmpty(request.getParameter("movieRelNames"))){
                String[] movieRelNames=request.getParameter("movieRelNames").split(",");
                for (int i = 0; i <movieRelNames.length ; i++) {
                    MovieRelNameInfo movieRelNameInfo=new MovieRelNameInfo();
                    movieRelNameInfo.setMovieRelNameId(UUID.randomUUID().toString());
                    movieRelNameInfo.setMovieId(movieId);
                    movieRelNameInfo.setMovieRelName(movieRelNames[i]);
                    //System.out.println(movieRelNameInfo);
                    movieRelNameInfoDao.insertMovieRelNameInfo(movieRelNameInfo);
                }
            }

            //电影海报和剧照
            for(MultipartFile file:files){
                MoviePictureInfo moviePictureInfo=new MoviePictureInfo();
                moviePictureInfo.setMoviePictureId(UUID.randomUUID().toString());
                moviePictureInfo.setMovieId(movieId);
                moviePictureInfo.setMoviePictureData(FileUtil.encodeBase64File(file));
                //System.out.println(moviePictureInfo);
                moviePictureInfoDao.insertMoviePictureInfo(moviePictureInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            throw new Exception(e.getMessage()) ;
        }

        return "success";
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value = "/queryMovieInfo",method = RequestMethod.POST)
    public Map<String,Object> queryMovieInfo(@RequestBody Map<String, Object> param) throws Exception{

        Map<String,Object> resultMap=new HashMap<String,Object>();

        //此处PageHelper有个大坑，它无法对关联查询的sql正确分页，故不再采用
        try{
            int page=Integer.valueOf(param.get("page").toString());
            int pageSize=Integer.valueOf(param.get("pageSize").toString());
            System.out.println(page+" "+pageSize);
            //PageHelper.startPage(page,pageSize);
            //PageHelper.orderBy("ALT_DATE DESC");

            int start=(page-1)*pageSize+1;
            int end=page*pageSize;

            Map<String,Object> map=new HashMap<String,Object>();
            map.put("userId",userId);
            map.put("start",start);
            map.put("end",end);
            int count=movieBasicInfoDao.queryMovieInfoByUserIdCount(map);
            List<MovieInfo> movieInfoList=null;
            if(count>0){
                movieInfoList=movieBasicInfoDao.queryMovieInfoByUserId(map);
            }

            //<MovieInfo> pageInfo=new PageInfo<MovieInfo>(movieInfoList);
            //System.out.println(movieInfoList);

            resultMap.put("list",movieInfoList);
            resultMap.put("count",count);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            throw new Exception(e.getMessage()) ;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "queryMovieDataByMovieId",method = RequestMethod.GET)
    public MovieShowInfoAll queryMovieDataByMovieId(@RequestParam("movieId")String movieId)throws Exception{
        MovieShowInfoAll movieShowInfoAll=new MovieShowInfoAll();
        MovieBasicInfo movieBasicInfo=movieBasicInfoDao.queryMovieInfoByMovieId(movieId);
        if(movieBasicInfo==null){
            throw new Exception("加载失败");
        }
        movieShowInfoAll.setMovieId(movieBasicInfo.getMovieId());
        movieShowInfoAll.setMovieName(movieBasicInfo.getMovieName());
        movieShowInfoAll.setMovieEnglishName(movieBasicInfo.getMovieEnglishName());
        movieShowInfoAll.setMovieCountry(movieBasicInfo.getMovieCountry());
        movieShowInfoAll.setMovieDBScore(Double.valueOf(movieBasicInfo.getMovieDBScore()));
        movieShowInfoAll.setMovieShowTime(DateUtil.StringYMDToDate(movieBasicInfo.getMovieShowTime()));
        movieShowInfoAll.setMovieContent(movieBasicInfo.getMovieContent());
        MovieUserInfo movieUserInfo=movieUserInfoDao.queryMovieUserInfoByMovieIdAndUserId(movieId,userId);
        if(movieUserInfo!=null){
            movieShowInfoAll.setMovieIsWatch("true".equals(movieUserInfo.getMovieIsWatch())?true:false);
            movieShowInfoAll.setMovieWatchTime(DateUtil.StringYMDToDate(movieUserInfo.getMovieWatchTime()));
        }
        List<MovieTypeInfo> movieTypeInfoList=movieTypeInfoDao.queryMovieTypeInfoByMovieId(movieId);
        List<String> movieTypes=new ArrayList<String>();
        if(movieTypeInfoList!=null&&movieTypeInfoList.size()>0){
            for (MovieTypeInfo movieTypeInfo:movieTypeInfoList) {
                movieTypes.add(movieTypeInfo.getMovieCode());
            }
        }
        movieShowInfoAll.setMovieTypes(movieTypes);
        //System.out.println(movieShowInfoAll);
        List<MovieRelNameInfo> movieRelNameInfoList= movieRelNameInfoDao.queryMovieRelNameInfoByMovieId(movieId);

        List<Map<String,String>>movieRelNames=new ArrayList<>();
        if(movieRelNameInfoList!=null&&movieRelNameInfoList.size()>0){
            for (MovieRelNameInfo movieRelName:movieRelNameInfoList) {
                Map<String,String>map=new HashMap<>();
                map.put("movieRelName",movieRelName.getMovieRelName());
                movieRelNames.add(map);
            }
            movieShowInfoAll.setMovieRelNames(movieRelNames);
        }else{
            Map<String,String>map=new HashMap<>();
            map.put("movieRelName","");
            movieRelNames.add(map);
            movieShowInfoAll.setMovieRelNames(movieRelNames);
        }


        return movieShowInfoAll;
    }
}
