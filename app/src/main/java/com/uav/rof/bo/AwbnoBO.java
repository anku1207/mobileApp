package com.uav.rof.bo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uav.rof.activity.ConnectionVO;
import com.uav.rof.vo.ServiceTypeVO;
import com.uav.rof.vo.ShipmentUnitVO;

import java.io.Serializable;
import java.util.HashMap;

public class AwbnoBO  implements Serializable {

    public static ConnectionVO getawbnoverify(String awbno){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("waybillNumber", awbno);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getLastMileByAWB");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }



    public static ConnectionVO getVendorByMemberId(Integer memberid){
        HashMap<String, Object> params = new HashMap<String, Object>();


        params.put("memberId",memberid);

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getVendorByMemberId");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }


    public static ConnectionVO getAWBByAttorney(String awbno ,Integer memberid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("waybillNumber", awbno);
        params.put("anonymousInteger", memberid);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getAWBByAttorney");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }



    public static ConnectionVO getawbVerifyToPickup(String awbno){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("awbNo", awbno);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getawbVerifyToPickup");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;
    }



    public static ConnectionVO vendorBookingPickup(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("vendorBookingPickup");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
    }




}