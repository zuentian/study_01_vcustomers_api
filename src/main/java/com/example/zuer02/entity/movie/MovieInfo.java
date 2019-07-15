package com.example.zuer02.entity.movie;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MovieInfo {
    private String movieId;
    private String movieName;
    private String movieEnglishName;
    private String movieCountry;
    private String movieDBScore;
    private String movieContent;
    private String movieShowTime;
    //private String movieTypeCode;
    private String movieIsWatch;
    private String movieWatchTime;


}
