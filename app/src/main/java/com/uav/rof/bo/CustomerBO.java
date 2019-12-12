package com.uav.rof.bo;

import com.uav.rof.activity.ConnectionVO;

import java.io.Serializable;
import java.util.HashMap;

public class CustomerBO   implements Serializable {

    public static ConnectionVO getCustomerSearch(){



        ConnectionVO connectionVO = new ConnectionVO();
        connectionVO.setMethodName("getCustomerByAutoSuggest");
        connectionVO.setRequestType(ConnectionVO.REQUEST_POST);
        connectionVO.setSearchKeyName("name");
        connectionVO.setTitle("Customer");

        return connectionVO;

    }
}
