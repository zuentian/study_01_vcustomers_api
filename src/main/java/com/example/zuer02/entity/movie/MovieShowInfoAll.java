package com.example.zuer02.entity.movie;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private String movieShowTime;
    //private String movieTypeCode;
    private Boolean movieIsWatch;
    private String movieWatchTime;

    private List<String> movieTypes;
    private List<Map<String,String>>movieRelNames;
    private List<MoviePictureInfoBase> files;
}
