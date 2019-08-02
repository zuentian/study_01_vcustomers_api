package com.example.zuer02.dao;


import com.example.zuer02.entity.DictInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictInfoDao {
    List<DictInfo> queryDictInfo(Map<String,Object> map);

    int addDictInfo(DictInfo dictInfo);

    DictInfo getDictTypeContent(@Param("dictType") String dictType);

    DictInfo queryDictInfoByDictId(@Param("dictId") String dictId);

    int editDictInfo(DictInfo dictInfo);

    int deleteDictInfoByDictId(@Param("dictId") String dictId);

    DictInfo queryDictInfoByDictTypeAndDictId(@Param("dictType") String dictType ,@Param("dictCode")String dictCode);

    List<DictInfo> queryDictInfoByDictType(@Param("dictType") String dictType);
}
