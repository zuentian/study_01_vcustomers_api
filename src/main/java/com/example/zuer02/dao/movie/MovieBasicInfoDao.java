package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieBasicInfo;
import com.example.zuer02.entity.movie.MovieInfo;
import com.example.zuer02.entity.movieReport.CountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MovieBasicInfoDao {
    int insertMovieBasicInfo(MovieBasicInfo movieBasicInfo);

    List<MovieInfo> queryMovieInfoByUserId(Map<String, Object> map);

    MovieBasicInfo queryMovieInfoByMovieId(@Param("movieId") String movieId);


    int queryMovieInfoByUserIdCount(Map<String, Object> map);

    int updateMovieBasicInfoByMovieId(MovieBasicInfo movieInfo );

    int deleteMovieBasicInfoByMovieId(@Param("movieId")String movieId);

    List<CountInfo> getMovieCountryCount();
}
