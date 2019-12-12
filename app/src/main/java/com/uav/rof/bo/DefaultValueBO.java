package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;

public class DefaultValueBO implements Serializable {

    public static ConnectionVO getDefaultValueCache(){


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getDefaultValue");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);

        return connectionVO;

    }
}
