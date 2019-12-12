package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;

public class ShipmentSaveBO  implements Serializable {

    public static ConnectionVO getsaveShipmentUnit(){

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("saveShipmentUnit");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;

    }



    public static ConnectionVO saveBooking(){

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("saveBooking");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        return connectionVO;

    }
}