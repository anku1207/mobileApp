package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class PickupConfirmationBO implements Serializable {

    public static ConnectionVO getConfirmationVendor(String memberid){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("anonymousInteger", memberid);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("VendorListByDateAndPickBy");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;

    }


    public static ConnectionVO sendMailToVendor(String attorneyId){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("attorneyID", attorneyId);
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("pickupConfirmationMail");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;

    }
}
