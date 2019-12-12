package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class AwbQueryBO implements Serializable {

    public static ConnectionVO getAwbQueryData(String awbno){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("awbNo", awbno);
        params.put("anonymousString", "awbno");

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getShipmentByInformationByAwb");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;

    }


}
