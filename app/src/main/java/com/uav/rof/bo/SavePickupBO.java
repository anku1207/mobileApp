package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;

public class SavePickupBO implements Serializable {


    public static ConnectionVO getSavePickup(){

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("savePickup");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;
   }
}
