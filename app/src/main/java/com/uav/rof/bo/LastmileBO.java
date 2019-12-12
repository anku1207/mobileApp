package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class LastmileBO implements Serializable {

    public static ConnectionVO getCache(){


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getForwarderListCache");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);


        return connectionVO;

    }

}
