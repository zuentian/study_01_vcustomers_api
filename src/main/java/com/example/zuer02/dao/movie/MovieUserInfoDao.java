package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieUserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieUserInfoDao {

    int insertMovieUserInfo(MovieUserInfo movieUserInfo);
}
