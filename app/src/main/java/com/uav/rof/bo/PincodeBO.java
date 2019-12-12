package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class PincodeBO  implements Serializable {

    public static ConnectionVO getPincode(String pincode){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("pincode", pincode);

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getCityByPincode");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;

    }
}
