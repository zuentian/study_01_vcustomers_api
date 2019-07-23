package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovieUserInfoDao {

    int insertMovieUserInfo(MovieUserInfo movieUserInfo);

    MovieUserInfo queryMovieUserInfoByMovieIdAndUserId(@Param("movieId") String movieId, @Param("userId") String userId);

    int updateMovieUserInfoByUserIdAndMovied(MovieUserInfo movieUserInfo);

    int deleteMovieUserInfoByMovieIdAndUserId(@Param("movieId") String movieId, @Param("userId") String userId);
}
