package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MovieRelNameInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieRelNameInfoDao {
    int insertMovieRelNameInfo(MovieRelNameInfo movieRelNameInfo);
}
