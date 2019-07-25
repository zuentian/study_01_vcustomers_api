package com.example.zuer02.entity.movieReport;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class CountInfo {

    private int value;
    private String name;
    private boolean selected=false;

}
