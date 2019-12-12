package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class IpAddressBO implements Serializable {

    public static ConnectionVO getIpAddtess(){
        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("checkStatus");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
     return connectionVO;
 }
}
