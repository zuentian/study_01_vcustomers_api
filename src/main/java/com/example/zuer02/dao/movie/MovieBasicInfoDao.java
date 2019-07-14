package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieBasicInfo;
import com.example.zuer02.entity.movie.MovieInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieBasicInfoDao {
    int insertMovieBasicInfo(MovieBasicInfo movieBasicInfo);

    List<MovieInfo> queryMovieInfo(@Param("userId") String userId);
}
