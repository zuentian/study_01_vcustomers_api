package com.example.zuer02.entity.movie;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class MovieShowInfoAll {
    private String movieId;
    private String movieName;
    private String movieEnglishName;
    private String movieCountry;
    private double movieDBScore;
    private String movieContent;
    private Date movieShowTime;
    //private String movieTypeCode;
    private Boolean movieIsWatch;
    private Date movieWatchTime;

    private List<String> movieTypes;
    private List<Map<String,String>>movieRelNames;
}
