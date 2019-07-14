package com.example.zuer02.entity.movie;

import lombok.Data;

import java.util.Date;

@Data
public class MovieBasicInfo {

    private String movieId;
    private String movieName;
    private String movieEnglishName;
    private String movieCountry;
    private String movieDBScore;
    private String movieContent;
    private String movieShowTime;
    private Date crtDate;
    private Date altDate;
}
