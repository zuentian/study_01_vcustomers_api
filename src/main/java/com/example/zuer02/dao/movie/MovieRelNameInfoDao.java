package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieRelNameInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieRelNameInfoDao {
    int insertMovieRelNameInfo(MovieRelNameInfo movieRelNameInfo);

    List<MovieRelNameInfo> queryMovieRelNameInfoByMovieId(@Param("movieId") String movieId);

    int deleteMovieRelNameInfoByMovieId(@Param("movieId") String movieId);
}
