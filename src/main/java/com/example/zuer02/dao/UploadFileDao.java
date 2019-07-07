package com.example.zuer02.dao;

import com.example.zuer02.entity.UploadFileData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileDao {
    int insertUploadFileData(UploadFileData uploadFileData);
}
