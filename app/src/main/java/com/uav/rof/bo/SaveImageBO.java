package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class SaveImageBO implements Serializable {

    public static ConnectionVO getImage(){

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("savePickupImage");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);


        return connectionVO;

    }
}
