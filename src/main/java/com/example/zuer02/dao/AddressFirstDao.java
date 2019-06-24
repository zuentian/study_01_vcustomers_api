package com.example.zuer02.dao;

import com.example.zuer02.entity.AddressFirst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressFirstDao {
    AddressFirst queryAddressListForFirstName(@Param("firstName") String firstName,@Param("seqId") String seqId);

    int insertAddressFirst(AddressFirst addressFirst);

    List<AddressFirst> queryAddressListById(String id);
}
