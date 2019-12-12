package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;

public class ServiceTypeBO implements Serializable {

    public static ConnectionVO getServiceTypeCache(){


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getServiceTypeListCache");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);

        return connectionVO;

    }

}
