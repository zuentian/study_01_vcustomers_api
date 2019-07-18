package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MoviePictureInfo;
import com.example.zuer02.entity.movie.MoviePictureInfoBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MoviePictureInfoDao {
    int insertMoviePictureInfo(MoviePictureInfo moviePictureInfo);

    List<MoviePictureInfoBase> queryMoviePictureInfoByMovieId(@Param("movieId") String movieId);
}
