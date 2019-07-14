package com.example.zuer02.entity.movie;

import lombok.Data;

@Data
public class MovieUserInfo {


    private String userId;
    private String movieId;
    private String movieIsWatch;
    private String movieWatchTime;
}
