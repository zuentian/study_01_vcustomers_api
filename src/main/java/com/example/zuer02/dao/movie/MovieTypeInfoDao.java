package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieTypeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieTypeInfoDao {
    int insertMovieTypeInfo(MovieTypeInfo movieTypeInfo);

    List<MovieTypeInfo> queryMovieTypeInfoByMovieId(@Param("movieId") String movieId);

    int deleteMovieTypeInfoByMovieId(@Param("movieId") String movieId);

}
