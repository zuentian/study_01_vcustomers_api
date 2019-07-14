package com.example.zuer02.dao.movie;

import com.example.zuer02.entity.movie.MoviePictureInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MoviePictureInfoDao {
    int insertMoviePictureInfo(MoviePictureInfo moviePictureInfo);
}
