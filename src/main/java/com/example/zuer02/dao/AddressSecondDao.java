package com.example.zuer02.dao;

import com.example.zuer02.entity.AddressSecond;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public   interface AddressSecondDao {
    AddressSecond queryAddressListForSecondName(String secondName,String seqId);

    int insertAddressSecond(AddressSecond addressSecond);

    List<AddressSecond> queryAddressSecondListById(String id);
}
