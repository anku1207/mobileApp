package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;

public class ShipmentTypeBO implements Serializable {

    public static ConnectionVO getShipmentType(){


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getShipmentTypeListCache");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);

        return connectionVO;

    }

}