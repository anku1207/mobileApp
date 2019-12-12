package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class PaymentTypeBO implements Serializable {

    public static ConnectionVO getPaymentType(){


        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getPaymentTypeListCache");
        connectionVO.setRequestType(ConnectionVO.REQUEST_GET);

        return connectionVO;

    }

}