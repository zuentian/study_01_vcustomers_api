package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieTypeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieTypeInfoDao {
    int insertMovieTypeInfo(MovieTypeInfo movieTypeInfo);
}
