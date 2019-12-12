package com.uav.exceptions;

import android.content.Context;
import android.text.StaticLayout;
import android.view.View;

import com.uav.rof.activity.Booking;
import com.uav.rof.bo.ExceptionHandlingBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.util.Utility;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionsNotification {


    public static void ExceptionHandling(Context context, String e){
        VolleyUtils.makeJsonObjectRequest(context, ExceptionHandlingBO.sendExceptionToServer(e), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){

                }else {

                }

            }
        });

    }
}
