package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class ExceptionHandlingBO implements Serializable {


    public static ConnectionVO sendExceptionToServer( String exe){

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("exe", exe);


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("printException");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);
        return connectionVO;

    }
}
