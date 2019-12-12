package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class MemberBO  implements Serializable {

    public static ConnectionVO getMember(String emailId){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("emailId", emailId);

        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("login");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setParams(params);

        return connectionVO;

    }

}
