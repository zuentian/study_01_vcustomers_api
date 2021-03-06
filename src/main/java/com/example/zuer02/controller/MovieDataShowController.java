package com.example.zuer02.controller;


import com.example.zuer02.dao.movie.*;
import com.example.zuer02.entity.User;
import com.example.zuer02.entity.movie.*;
import com.example.zuer02.utils.DateUtil;
import com.example.zuer02.utils.UploadFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.File;
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

    @Value("${local.vueIp}")
    private String vueIp;

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
    @RequestMapping(value="/addMovieData", method= RequestMethod.POST)
    public String addMovieData(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {


        try{
            //电影基本信息
            String movieId=UUID.randomUUID().toString();
            MovieBasicInfo movieBasicInfo=getMovieBascInfo(movieId,request);
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
            if(files!=null&&files.length>0){
                for(MultipartFile file:files){
                    MoviePictureInfo moviePictureInfo=new MoviePictureInfo();
                    String moviePictureId=UUID.randomUUID().toString();
                    moviePictureInfo.setMoviePictureId(moviePictureId);
                    moviePictureInfo.setMovieId(movieId);
                    moviePictureInfo.setMovieName(file.getOriginalFilename());
                    moviePictureInfo.setMoviePictureUrl(File.separator+vueIp+ File.separator+UploadFile.uploadMultipartFile(file,moviePictureId));
                    moviePictureInfoDao.insertMoviePictureInfo(moviePictureInfo);
                }
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
    public Map<String,Object> queryMovieInfo(@RequestBody Map<String, Object> param,HttpServletRequest request) throws Exception{

        String movieCountry=(String) (param.get("movieCountry"));
        String movieDBScoreEnd=(String) (param.get("movieDBScoreEnd"));
        String movieDBScoreStart=(String) (param.get("movieDBScoreStart"));
        String movieIsWatch=(String) (param.get("movieIsWatch"));
        String movieName=(String) (param.get("movieName"));
        String movieShowTimeStart= (String) param.get("movieShowTimeStart");
        String movieShowTimeEnd= (String) param.get("movieShowTimeEnd");
        String movieWatchTimeStart=(String) (param.get("movieWatchTimeStart"));
        String movieWatchTimeEnd=(String) (param.get("movieWatchTimeEnd"));

         Map<String,Object> resultMap=new HashMap<String,Object>();

        //此处PageHelper有个大坑，它无法对关联查询的sql正确分页，故不再采用
        try{
            int page=Integer.valueOf(param.get("page").toString());
            int pageSize=Integer.valueOf(param.get("pageSize").toString());
            //System.out.println(page+" "+pageSize);
            //PageHelper.startPage(page,pageSize);
            //PageHelper.orderBy("ALT_DATE DESC");

            int start=(page-1)*pageSize+1;
            int end=page*pageSize;

            Map<String,Object> map=new HashMap<String,Object>();
            map.put("userId",userId);
            map.put("start",start);
            map.put("end",end);
            map.put("movieCountry",(String)param.get("movieCountry"));
            map.put("movieDBScoreEnd",(String)param.get("movieDBScoreEnd"));
            map.put("movieDBScoreStart",(String)param.get("movieDBScoreStart"));
            map.put("movieIsWatch",(String)param.get("movieIsWatch"));
            map.put("movieName",(String)param.get("movieName"));
            map.put("movieShowTimeStart",(String)param.get("movieShowTimeStart"));
            map.put("movieShowTimeEnd",(String)param.get("movieShowTimeEnd"));
            map.put("movieWatchTimeStart",(String)param.get("movieWatchTimeStart"));
            map.put("movieWatchTimeEnd",(String)param.get("movieWatchTimeEnd"));

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
    @RequestMapping(value = "queryMovieDataByMovieId",method = RequestMethod.POST)
    public Map<String,Object> queryMovieDataByMovieId(@RequestBody Map<String, Object> param)throws Exception{
        String movieId=String.valueOf(param.get("movieId"));

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
        movieShowInfoAll.setMovieShowTime(movieBasicInfo.getMovieShowTime());
        movieShowInfoAll.setMovieContent(movieBasicInfo.getMovieContent());
        MovieUserInfo movieUserInfo=movieUserInfoDao.queryMovieUserInfoByMovieIdAndUserId(movieId,userId);
        if(movieUserInfo!=null){
            movieShowInfoAll.setMovieIsWatch("true".equals(movieUserInfo.getMovieIsWatch())?true:false);
            movieShowInfoAll.setMovieWatchTime(movieUserInfo.getMovieWatchTime());
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

        List<MoviePictureInfoBase> moviePictureInfoBase=moviePictureInfoDao.queryMoviePictureInfoBaseByMovieId(movieId);
        movieShowInfoAll.setFiles(moviePictureInfoBase);
        Map<String,Object>map=new HashMap<>();
        map.put("list",movieShowInfoAll);
        return map;
    }


    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/updateMovieData", method= RequestMethod.POST)
    public String updateMovieData(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {

        try{
            //更新电影基本信息
            String movieId=request.getParameter("movieId");
            MovieBasicInfo movieBasicInfo=getMovieBascInfo(movieId,request);
            int n=movieBasicInfoDao.updateMovieBasicInfoByMovieId(movieBasicInfo);
            if(n<0){
                throw new Exception("更新电影基本信息失败") ;
            }
            //更新用户观影信息
            MovieUserInfo movieUserInfo=new MovieUserInfo();
            movieUserInfo.setUserId(userId);//先写死
            movieUserInfo.setMovieId(movieId);//
            movieUserInfo.setMovieIsWatch(request.getParameter("movieIsWatch"));
            movieUserInfo.setMovieWatchTime(DateUtil.gmtToStringYMD(request.getParameter("movieWatchTime")));
            movieUserInfoDao.updateMovieUserInfoByUserIdAndMovied(movieUserInfo);

            //更新电影类型
            movieTypeInfoDao.deleteMovieTypeInfoByMovieId(movieId);
            if(!StringUtils.isEmpty(request.getParameter("movieTypes"))){
                String[] movieTypes= request.getParameter("movieTypes").split(",");
                for (int i = 0; i < movieTypes.length; i++) {
                    MovieTypeInfo movieTypeInfo=new MovieTypeInfo();
                    movieTypeInfo.setMovieTypeId(UUID.randomUUID().toString());
                    movieTypeInfo.setMovieId(movieId);
                    movieTypeInfo.setMovieCode(movieTypes[i]);
                    movieTypeInfoDao.insertMovieTypeInfo(movieTypeInfo);
                }
            }


            //电影相关人物信息moviePictureNotDelete
            movieRelNameInfoDao.deleteMovieRelNameInfoByMovieId(movieId);
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

            //更新电影海报和剧照
            Map<String,Object> map=new HashMap<>();
            if(!StringUtils.isEmpty(request.getParameter("moviePictureNotDelete"))){
                String[] moviePictureNotDelete=request.getParameter("moviePictureNotDelete").split(",");
                if(moviePictureNotDelete.length>0){
                    map.put("moviePictureNotDelete",moviePictureNotDelete);
                }

            }
            map.put("movieId",movieId);
            //List<MoviePictureInfo> moviePictureInfoList =moviePictureInfoDao.queryMoviePictureInfoByMovieId(movieId);
            List<MoviePictureInfo> moviePictureInfoList =moviePictureInfoDao.queryMoviePictureInfoMapByMovieId(map);
            if(moviePictureInfoList!=null&&moviePictureInfoList.size()>0){
                for (MoviePictureInfo moviePictureInfo:moviePictureInfoList) {
                    String path=moviePictureInfo.getMoviePictureUrl();
                    path=path.replace(File.separatorChar+vueIp,"");
                    boolean flag=UploadFile.deleteFile(path);
                    if(flag){
                        moviePictureInfoDao.deleteMoviePictureInfoByMoviePictureId(moviePictureInfo.getMoviePictureId());
                    }else{
                        throw new Exception("更新电影图片时删除原图片失败") ;
                    }
                }
            }
            if(files!=null&&files.length>0){
                for(MultipartFile file:files){
                    MoviePictureInfo moviePictureInfo=new MoviePictureInfo();
                    String moviePictureId=UUID.randomUUID().toString();
                    moviePictureInfo.setMoviePictureId(moviePictureId);
                    moviePictureInfo.setMovieId(movieId);
                    moviePictureInfo.setMovieName(file.getOriginalFilename());
                    moviePictureInfo.setMoviePictureUrl(File.separator+vueIp+ File.separator+UploadFile.uploadMultipartFile(file,moviePictureId));
                    moviePictureInfoDao.insertMoviePictureInfo(moviePictureInfo);
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            throw new Exception(e.getMessage()) ;
        }

        return "success";
    }

    private MovieBasicInfo getMovieBascInfo(String movieId,HttpServletRequest request){
        MovieBasicInfo movieBasicInfo=new MovieBasicInfo();
        movieBasicInfo.setMovieId(movieId);
        movieBasicInfo.setMovieName(request.getParameter("movieName"));
        movieBasicInfo.setMovieEnglishName(request.getParameter("movieEnglishName"));
        movieBasicInfo.setMovieCountry(request.getParameter("movieCountry"));
        movieBasicInfo.setMovieDBScore(request.getParameter("movieDBScore"));
        movieBasicInfo.setMovieShowTime(DateUtil.gmtToStringYMD(request.getParameter("movieShowTime")));
        movieBasicInfo.setMovieContent(request.getParameter("movieContent"));
        return movieBasicInfo;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/queryMoviDetailByMovieId", method=RequestMethod.GET)
    public Map<String,Object> queryMoviDetailByMovieId(@RequestParam("movieId") String movieId) {
        Map<String,Object> resultMap=new HashMap<>();
        MovieBasicInfo movieBasicInfo=movieBasicInfoDao.queryMovieInfoByMovieId(movieId);
        resultMap.put("MovieBasicInfo",movieBasicInfo);
        String movieShowTime=movieBasicInfo.getMovieShowTime();
        String titleMovieYear=DateUtil.getYear(DateUtil.StringYMDToDate(movieShowTime));
        resultMap.put("titleMovieYear",titleMovieYear);
        double movieDBScoreValue= Double.parseDouble(movieBasicInfo.getMovieDBScore());
        resultMap.put("movieDBScoreValue",movieDBScoreValue);

        List<MovieTypeInfo> movieTypeInfoList=movieTypeInfoDao.queryMovieTypeInfoByMovieId(movieId);
        String movieTypeAll="";
        if(movieTypeInfoList!=null&&movieTypeInfoList.size()>0){
            for (MovieTypeInfo movieTypeInfo:movieTypeInfoList) {
                MovieType movieType=movieTypeDao.queryMovieTypeByMovieCode(movieTypeInfo.getMovieCode());
                movieTypeAll+=movieType.getTypeName()+" / ";
            }
        }
        movieTypeAll=movieTypeAll.substring(0,movieTypeAll.lastIndexOf("/")).trim();
        resultMap.put("movieTypeAll",movieTypeAll);

        List<MovieRelNameInfo> movieRelNameInfoList= movieRelNameInfoDao.queryMovieRelNameInfoByMovieId(movieId);

        List<MovieRelNameInfo> movieRelPersons=new ArrayList<MovieRelNameInfo>();
        if(movieRelNameInfoList!=null&&movieRelNameInfoList.size()>0){
            for (MovieRelNameInfo movieRelName:movieRelNameInfoList) {
                movieRelPersons.add(movieRelName);
            }
        }
        resultMap.put("movieRelPersons",movieRelPersons);

        MovieUserInfo movieUserInfo=movieUserInfoDao.queryMovieUserInfoByMovieIdAndUserId(movieId,userId);
        if(movieUserInfo!=null){
            resultMap.put("movieIsWatch","true".equals(movieUserInfo.getMovieIsWatch())?true:false);
            resultMap.put("movieWatchTime",movieUserInfo.getMovieWatchTime());
        }
        int count=moviePictureInfoDao.queryMoviePictureInfoBaseCountByMovieId(movieId);
        resultMap.put("moviePictureInfoBaseCount",count);
        List<MoviePictureInfoBase> moviePictureInfoBase=moviePictureInfoDao.queryMoviePictureInfoBaseByMovieIdShow5(movieId);
        resultMap.put("moviePictureInfoBase",moviePictureInfoBase);
        return resultMap;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/queryMoviePictureDetail", method=RequestMethod.POST)
    public Map<String,Object> queryMoviePictureDetail(@RequestBody Map<String, Object> param) {
        String movieId=String.valueOf(param.get("movieId"));
        int currentPage=Integer.valueOf( param.get("currentPage").toString());
        int pageSize=Integer.valueOf(param.get("pageSize").toString());
        PageHelper.startPage(currentPage,pageSize);
        Map<String,Object> resultMap=new HashMap<>();
        List<MoviePictureInfoBase> moviePictureInfoBase=moviePictureInfoDao.queryMoviePictureInfoBaseByMovieId(movieId);
        PageInfo<MoviePictureInfoBase> pageInfo=new PageInfo<>(moviePictureInfoBase);
        resultMap.put("pageInfo",pageInfo);
        MovieBasicInfo movieBasicInfo=movieBasicInfoDao.queryMovieInfoByMovieId(movieId);
        resultMap.put("movieName",movieBasicInfo.getMovieName());
        return resultMap;
    }

    @Transactional(rollbackFor = { Exception.class })
    @RequestMapping(value="/deleteMoviDetailByMovieId", method=RequestMethod.POST)
    public String deleteMoviDetailByMovieId(@RequestBody Map<String, Object> param) throws Exception {
        String movieId=String.valueOf(param.get("movieId"));
        try{
            //删除基本信息
            movieBasicInfoDao.deleteMovieBasicInfoByMovieId(movieId);
            //删除用户观影信息
            movieUserInfoDao.deleteMovieUserInfoByMovieIdAndUserId(movieId,userId);
            //删除电影类型(多笔删除）
            movieTypeInfoDao.deleteMovieTypeInfoByMovieId(movieId);
            //删除电影相关人物(多笔删除)
            movieRelNameInfoDao.deleteMovieRelNameInfoByMovieId(movieId);
            //删除电影图片
            List<MoviePictureInfo> moviePictureInfoList =moviePictureInfoDao.queryMoviePictureInfoByMovieId(movieId);
            if(moviePictureInfoList!=null&&moviePictureInfoList.size()>0){
                for (MoviePictureInfo moviePictureInfo:moviePictureInfoList) {
                    String path=moviePictureInfo.getMoviePictureUrl();
                    path=path.replace(File.separatorChar+vueIp,"");
                    boolean flag=UploadFile.deleteFile(path);
                    if(flag){
                        moviePictureInfoDao.deleteMoviePictureInfoByMoviePictureId(moviePictureInfo.getMoviePictureId());
                    }else{
                        throw new Exception("删除原图片失败") ;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//就是这一句了，加上之后，如果doDbStuff2()抛了异常,     doDbStuff1()是会回滚的                                                                                   //doDbStuff1()是会回滚的
            throw new Exception(e.getMessage()) ;
         }

        return "success";
    }
}
