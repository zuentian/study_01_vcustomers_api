package com.example.zuer02.controller;

import com.example.zuer02.dao.AddressFirstDao;
import com.example.zuer02.dao.AddressSecondDao;
import com.example.zuer02.dao.AddressThirdDao;
import com.example.zuer02.entity.AddressFirst;
import com.example.zuer02.entity.AddressNode;
import com.example.zuer02.entity.AddressSecond;
import com.example.zuer02.entity.AddressThird;
import oracle.net.jdbc.TNSAddress.AddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@EnableAutoConfiguration
@RequestMapping(value="")
public class AddressListController {
    @Autowired
    private AddressFirstDao addressFirstDao;
    @Autowired
    private AddressSecondDao addressSecondDao;
    @Autowired
    private AddressThirdDao addressThirdDao;

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/addAddressList", method = RequestMethod.POST)
    public String addAddressList(@RequestBody Map<String, Object> param) throws Exception {

        String first=(String)param.get("first");
        String second=(String)param.get("second");
        String thrid=(String)param.get("thrid");
        int repeatFlag=0;
        if(!"".equals(first)){

            String firstId="";

            AddressFirst addressFirstOld=addressFirstDao.queryAddressListForFirstName(first,"0000");


            if(null!=addressFirstOld){
                firstId=addressFirstOld.getAddressFirstId();
                repeatFlag++;
            }else{
                firstId= UUID.randomUUID().toString();
                AddressFirst addressFirst=new AddressFirst();
                addressFirst.setAddressFirstId(firstId);
                addressFirst.setAddressFirstName(first);
                addressFirst.setAddressFirstSeqId("0000");
                int i=addressFirstDao.insertAddressFirst(addressFirst);
                System.out.println("一级城市添加结果："+i);
            }

            if(!"".equals(second)){

                String secondId="";
                AddressSecond addressSecondOld=addressSecondDao.queryAddressListForSecondName(second,firstId);

                if(null!=addressSecondOld){
                    secondId=addressSecondOld.getAddressSecondId();
                    repeatFlag++;
                }else{
                    secondId=UUID.randomUUID().toString();
                    AddressSecond addressSecond=new AddressSecond();
                    addressSecond.setAddressSecondId(secondId);
                    addressSecond.setAddressSecondSeqId(firstId);
                    addressSecond.setAddressSecondName(second);
                    int i=addressSecondDao.insertAddressSecond(addressSecond);
                    System.out.println("二级城市添加结果："+i);
                }


                if(!"".equals(thrid)){
                    String thirdId="";
                    AddressThird addressThirdOld=addressThirdDao.queryAddressListForThirdName(thrid,secondId);
                    if(null!=addressThirdOld){
                        thirdId=addressThirdOld.getAddressThirdId();
                        repeatFlag++;
                    }else{
                        thirdId=UUID.randomUUID().toString();
                        AddressThird addressThird=new AddressThird();
                        addressThird.setAddressThirdId(thirdId);
                        addressThird.setAddressThirdSeqId(secondId);
                        addressThird.setAddressThirdName(thrid);
                        int i=addressThirdDao.insertAddressThrid(addressThird);
                        System.out.println("三级城市添加结果："+i);
                    }
                }
            }

        }
        if(repeatFlag==3){
            return "repeat";
        }

        return "success";
    }

    @Transactional(rollbackFor = {Exception.class})
    @RequestMapping(value = "/queryAddressList", method = RequestMethod.POST)
    public List<AddressNode> queryAddressList(@RequestBody Map<String, Object> param) throws Exception {

        System.out.println("开始城市查询！！");

        List<AddressNode> addressNodeList=new ArrayList<AddressNode>();
        List<AddressFirst> addressFirstList=addressFirstDao.queryAddressListById("0000");
        if(null!=addressFirstList&&addressFirstList.size()>0){
            for(AddressFirst addressFirst:addressFirstList){
                AddressNode addressNode1=new AddressNode();
                List<AddressSecond> addressSecondList =addressSecondDao.queryAddressSecondListById(addressFirst.getAddressFirstId());

                //System.out.println("addressSecondList="+addressSecondList);
                if(addressSecondList!=null&&addressSecondList.size()>0){
                    for(AddressSecond addressSecond:addressSecondList){
                        AddressNode addressNode2=new AddressNode();
                        List<AddressThird> addressThirdList =addressThirdDao.queryAddressListById(addressSecond.getAddressSecondId());
                        //System.out.println("addressThirdList="+addressThirdList);
                        if(addressThirdList!=null&&addressThirdList.size()>0){

                            for(AddressThird addressThird:addressThirdList){
                                AddressNode addressNode3=new AddressNode();
                                addressNode3.setValue(addressThird.getAddressThirdId());
                                addressNode3.setLabel(addressThird.getAddressThirdName());

                                if(addressNode2.getChildren()==null){
                                    addressNode2.setChildren(new ArrayList<AddressNode>());
                                }
                                addressNode2.getChildren().add(addressNode3);
                            }

                        }
                        addressNode2.setValue(addressSecond.getAddressSecondId());
                        addressNode2.setLabel(addressSecond.getAddressSecondName());
                        if(addressNode1.getChildren()==null){
                            addressNode1.setChildren(new ArrayList<AddressNode>());
                        }
                        addressNode1.getChildren().add(addressNode2);

                    }
                }
                addressNode1.setLabel(addressFirst.getAddressFirstName());
                addressNode1.setValue(addressFirst.getAddressFirstId());
                addressNodeList.add(addressNode1);
            }
        }

        System.out.println(addressNodeList);

        return addressNodeList;
    }
}
