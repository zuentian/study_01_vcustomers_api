package com.example.zuer02.controller;


import com.example.zuer02.dao.DictInfoDao;
import com.example.zuer02.entity.DictInfo;
import com.example.zuer02.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EnableAutoConfiguration
@RestController
@RequestMapping(value = "DictInfo")
public class DictController {

    @Autowired
    DictInfoDao dictInfoDao;
    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/queryDictInfo", method = RequestMethod.POST)
    public PageInfo<DictInfo> queryDictInfo(@RequestBody Map<String, Object> param) throws Exception {

        int pageSize=(Integer)param.get("pageSize");
        int currentPage=(Integer)param.get("currentPage");
        String dictType=(String)param.get("dictType");
        PageHelper.startPage(currentPage,pageSize);
        PageHelper.orderBy("ALT_DATE desc");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("dictType",dictType);
        List<DictInfo> dictInfoList = dictInfoDao.queryDictInfo(map);
        PageInfo<DictInfo> pageInfo=new PageInfo<>(dictInfoList);
        return pageInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/addDictInfo",method = RequestMethod.POST)
    public void addDictInfo(@RequestBody Map<String, Object> param)throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        DictInfo  dictInfo= mapper.convertValue(param.get("dictInfoAdd"), DictInfo.class);

        String dictId = UUID.randomUUID().toString();
        dictInfo.setDictId(dictId);
        int i=dictInfoDao.addDictInfo(dictInfo);

    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/getDictTypeContent",method = RequestMethod.POST)
    public Map<String,Object> getDictTypeContent(@RequestBody Map<String, Object> param)throws Exception{

        Map<String,Object> resultMap=new HashMap<>();
        String dictType=(String)param.get("dictType");
        DictInfo  dictInfo=dictInfoDao.getDictTypeContent(dictType);
        if(dictInfo!=null){
            String dictTypeContent=dictInfo.getDictTypeContent();
            resultMap.put("dictTypeContent",dictTypeContent);
        }
        return resultMap;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/queryDictInfoByDictId",method = RequestMethod.POST)
    public Map<String,Object> queryDictInfoByDictId(@RequestBody Map<String, Object> param)throws Exception{

        Map<String,Object> resultMap=new HashMap<>();
        String dictId=(String)param.get("dictId");
        DictInfo dictInfo=dictInfoDao.queryDictInfoByDictId(dictId);
        resultMap.put("dictInfo",dictInfo);
        return resultMap;
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/editDictInfo",method = RequestMethod.POST)
    public void editDictInfo(@RequestBody Map<String, Object> param)throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        DictInfo  dictInfo= mapper.convertValue(param.get("dictInfoEdit"), DictInfo.class);
        int i=dictInfoDao.editDictInfo(dictInfo);
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/deleteDictInfoByDictId",method = RequestMethod.POST)
    public void deleteDictInfoByDictId(@RequestBody Map<String, Object> param)throws Exception{

        Map<String,Object> resultMap=new HashMap<>();
        String dictId=(String)param.get("dictId");
        int i=dictInfoDao.deleteDictInfoByDictId(dictId);
    }
}
