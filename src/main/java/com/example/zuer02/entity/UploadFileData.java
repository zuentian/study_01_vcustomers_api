package com.example.zuer02.entity;

import lombok.Data;

@Data
public class UploadFileData {

    private String id;
    private String fileName;
    private String fileSize;
    private String fileType;
    private String fileContent;
}
