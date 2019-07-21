package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MoviePictureInfo;
import com.example.zuer02.entity.movie.MoviePictureInfoBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MoviePictureInfoDao {
    int insertMoviePictureInfo(MoviePictureInfo moviePictureInfo);

    List<MoviePictureInfoBase> queryMoviePictureInfoBaseByMovieId(@Param("movieId") String movieId);
    List<MoviePictureInfo> queryMoviePictureInfoByMovieId(@Param("movieId") String movieId);

    int deleteMoviePictureInfoByMoviePictureId( @Param("moviePictureId")String moviePictureId);

    List<MoviePictureInfo> queryMoviePictureInfoMapByMovieId(Map<String,Object> map);

    int queryMoviePictureInfoBaseCountByMovieId(@Param("movieId")String movieId);

    List<MoviePictureInfoBase> queryMoviePictureInfoBaseByMovieIdShow5(@Param("movieId")  String movieId);
}
