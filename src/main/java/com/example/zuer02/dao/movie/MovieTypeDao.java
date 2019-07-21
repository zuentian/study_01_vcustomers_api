package com.example.zuer02.dao.movie;


import com.example.zuer02.entity.movie.MovieType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MovieTypeDao {
     List<MovieType> queryMovieType() ;

    MovieType queryMovieTypeByMovieCode(@Param("typeCode") String movieCode);
}
