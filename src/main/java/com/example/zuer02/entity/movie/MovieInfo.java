package com.example.zuer02.entity.movie;

import lombok.Data;

@Data
public class MovieInfo {
    private String movieId;
    private String movieName;
    private String movieEnglishName;
    private String movieCountry;
    private String movieDBScore;
    private String movieShowTime;
    //private String movieTypeCode;
    private String movieIsWatch;
    private String movieWatchTime;

}
