package com.example.zuer02.dao;

import com.example.zuer02.entity.AddressThird;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public   interface AddressThirdDao {
    AddressThird queryAddressListForThirdName(String thridName,String seqId);

    int insertAddressThrid(AddressThird addressThird);

    List<AddressThird> queryAddressListById(String id);
}
