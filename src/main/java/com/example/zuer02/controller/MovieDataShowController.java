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
            movieBasicInfo.setMovieEnglishName(request.getParameter("movieEngLishName"));
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
    public PageInfo<MovieInfo> queryMovieInfo(@RequestBody Map<String, Object> param) throws Exception{
        try{
            int page=Integer.valueOf(param.get("page").toString());
            int pageSize=Integer.valueOf(param.get("pageSize").toString());

            System.out.println(page+" "+pageSize);
            PageHelper.startPage(page,pageSize);
            PageHelper.orderBy("ALT_DATE DESC");
            List<MovieInfo> movieInfoList=movieBasicInfoDao.queryMovieInfo(userId);
            PageInfo<MovieInfo> pageInfo=new PageInfo<MovieInfo>(movieInfoList);
            //System.out.println(movieInfoList);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            throw new Exception(e.getMessage()) ;
        }
    }
}
