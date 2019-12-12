package com.uav.rof.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ApplicationConstant {


    public final static String AUTHKEY= "G4s4cCMx2aM7lky1";
    public final static String HTTPURL = "http://192.168.1.12:8080/rof/rest/stateless/";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_DIALOG_SINGLEBUTTON = "singlebutton";
    public static final String ACTION_DIALOG_TWOBUTTONS = "twobuttons";
    public static final String ACTION_DIALOG_THREEBUTTONS = "threebuttons";
    public static final String INTENT_EXTRA_CONNECTION = "connection";

    public  static final int SOCKET_TIMEOUT_MILLISEC = 60000;
    public static final String SHAREDPREFENCE = "rof";

    public static final String CACHE_SERVICETYPE="serviceType";
    public static final String CACHE_FORWARDER="forwarder";
    public static final String CACHE_SHIPMENTTYPE="shipmenttype";
    public static final String CACHE_PAYMENTTYPE="paymenttype";
    public static final String CACHE_DEFAULTVALUE="defaultvalue";
    public static final String CACHE_CAMERA_PREVIEW="camerapreview";



    public static final String ACL_VENDOR_BOOKING ="ACL_VENDOR_BOOKING_APP";
    public static final String ACL_BOOKING  ="ACL_BOOKING_APP";
    public static final String ACL_RTO_APP   ="ACL_RTO_APP";
    public static final String ACL_AWB_QUERY_APP ="ACL_AWB_QUERY_APP";
    public static final String ACL_PICKUP_APP  ="ACL_PICKUP_APP";
    public static final String ACL_BOOKING_RECEIVED_APP ="ACL_BOOKING_RECEIVED_APP";
    public static final String ACL_MOBILE_PICKUP_CONFIRMATION ="ACL_MOBILE_PICKUP_CONFIRMATION";











    public static final String DEFAULTVALUE_BOOKING="defaultbookingvalue";



    public static final String CACHE_PORT="port";
    public static final String CACHE_IPADDRESS="ipaddress";
    public static final String CACHE_PROTOCOL="protocol";
    public static final String CACHE_IPVALID="ipvalid";

    public static final String CACHE_MAMBERID="memberid";

    public static final String CACHE_ACTIVITYES="activityes";


    public static final String CACHE_ATTORNEYNAME="attorneyname";
    public static final String CACHE_ATTORNEYID="attorneyid";


    public static final String MENU_MAIN_TOP_KEY_BOOKING="booking";
    public static final String MENU_MAIN_TOP_KEY_RTO="rto";
    public static final String MENU_MAIN_TOP_KEY_AWBQUERY="awbquery";
    public static final String MENU_MAIN_TOP_KEY_PICKUP="pickup";
    public static final String MENU_MAIN_VENDOR_BOOKING="vendorbooking";
    public static final String MENU_MAIN_BOOKING_RECEIVED="bookingreceived";
    public static final String MENU_MAIN_PICKUP_CONFIRMATION="pickupconfirmation";


    public static final String MENU_SETTING_VERTICAL_KEY_IPSETTING="ipsetting";
    public static final String MENU_SETTING_VERTICAL_KEY_EXIT="exit";
    public static final String MENU_SETTING_CAMERA="camera";


    public static final String SINGLE_IMAGE_ACTION_USER="clickpicuser";
    public static final String SINGLE_IMAGE_ACTION_SERVER="getserverurlimage";





    public  static  String getHttpURL(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        String protocol= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROTOCOL,null);
        String ipAddress= (String)sharedPreferences.getString( ApplicationConstant.CACHE_IPADDRESS,null);
        String port= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PORT,null);

        if(protocol!=null && ipAddress != null && port!=null){
            return protocol+"://"+ipAddress + ":" + port + "/rof/rest/stateless/";
        }else{
            return HTTPURL;
        }

    }
}
