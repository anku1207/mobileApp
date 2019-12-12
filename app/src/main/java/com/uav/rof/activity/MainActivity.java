package com.uav.rof.activity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.GridView;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.rof.uav.adpater.ImageTextAdapter;
        import com.uav.exceptions.ExceptionsNotification;
        import com.uav.rof.R;
        import com.uav.rof.bo.DefaultValueBO;
        import com.uav.rof.bo.LastmileBO;
        import com.uav.rof.bo.PaymentTypeBO;
        import com.uav.rof.bo.ServiceTypeBO;
        import com.uav.rof.bo.ShipmentTypeBO;
        import com.uav.rof.constants.ApplicationConstant;
        import com.uav.rof.util.Utility;
        import com.uav.rof.vo.DataAdapterVO;
        import com.uav.rof.vo.DefaultVO;
        import com.uav.rof.volley.VolleyResponseListener;
        import com.uav.rof.volley.VolleyUtils;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static   int REQ_CODE_FORWARDER = 1001;
    GridView listView;
    SharedPreferences sharedPreferences;
    ImageView setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getforwarder();
        getServiceType();
        getShipmentType();
        getPaymentType();
        getDefaultValue();
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        getSupportActionBar().hide();

        listView=(GridView)findViewById(R.id.mainmenu);
        setting=(ImageView)findViewById(R.id.rof_setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,setting.class));
            }
        });











       /* SharedPreferences sharedPreferences1= getSharedPreferences("ServiceType", Context.MODE_PRIVATE);
        Toast.makeText(this, ""+sharedPreferences1.getString("servicetype",null), Toast.LENGTH_SHORT).show();*/

        /*final String[] s1={"Booking","RTO"};
        int[]image={R.drawable.rof_book_icon_menu,R.drawable.rof_rto};*/

        final ArrayList<DataAdapterVO>  dataList =  getDataList();
        ImageTextAdapter myAdapter=new ImageTextAdapter(this, dataList, R.layout.degin);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String s=(String)parent.getItemAtPosition(position);
                int i = position;
                try {

                    DataAdapterVO dataAdapterVO = (DataAdapterVO) dataList.get(position);
                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_TOP_KEY_BOOKING)) {

                        Intent intent = new Intent(MainActivity.this, Booking.class);
                        startActivity(intent);


                    }
                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_TOP_KEY_RTO)) {
                        Toast.makeText(MainActivity.this, "RTO", Toast.LENGTH_SHORT).show();
                    }
                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_TOP_KEY_AWBQUERY)) {
                        Intent intent = new Intent(MainActivity.this, AwbQuery.class);
                        startActivity(intent);
                    }
                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_VENDOR_BOOKING)) {
                        Intent intent = new Intent(MainActivity.this, VendorBooking.class);
                        startActivity(intent);
                    }

                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_BOOKING_RECEIVED)) {
                        Intent intent = new Intent(MainActivity.this, BookingReceived.class);
                        startActivity(intent);
                    }


                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_TOP_KEY_PICKUP)) {
                        startActivity(new Intent(MainActivity.this, PickupActivity.class));
                    }

                    if (dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_MAIN_PICKUP_CONFIRMATION)) {

                        startActivity(new Intent(MainActivity.this, PickupConfirmation.class));
                    }


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_SHORT).show();
                    ExceptionsNotification.ExceptionHandling(MainActivity.this, Utility.getStackTrace(e));
                }
            }
        });

    }
    public  ArrayList<DataAdapterVO> getDataList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();



        String jsonString= (String)sharedPreferences.getString( ApplicationConstant.CACHE_ACTIVITYES,null);
        try {
            JSONArray jsonArray= new JSONArray(jsonString);


            DataAdapterVO dataAdapterVO = new DataAdapterVO();


            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_BOOKING)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("Booking");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_TOP_KEY_BOOKING);
                    dataAdapterVO.setImage(R.drawable.rof_book_icon_menu);
                    datalist.add(dataAdapterVO);
                }else          if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_RTO_APP)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("RTO");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_TOP_KEY_RTO);
                    dataAdapterVO.setImage(R.drawable.rof_rto);
                    datalist.add(dataAdapterVO);
                } else   if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_AWB_QUERY_APP)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("AWB Query");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_TOP_KEY_AWBQUERY);
                    dataAdapterVO.setImage(R.drawable.rof_awb_query);
                    datalist.add(dataAdapterVO);
                }else  if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_PICKUP_APP)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("Pickup");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_TOP_KEY_PICKUP);
                    dataAdapterVO.setImage(R.drawable.vehicle);
                    datalist.add(dataAdapterVO);
                }else if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_VENDOR_BOOKING)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("Vendor Booking");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_VENDOR_BOOKING);
                    dataAdapterVO.setImage(R.drawable.vendor);
                    datalist.add(dataAdapterVO);
                }else  if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_BOOKING_RECEIVED_APP)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("Booking Received");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_BOOKING_RECEIVED);
                    dataAdapterVO.setImage(R.drawable.pickupreceive);
                    datalist.add(dataAdapterVO);
                }else  if(jsonObject1.getString("aclkey").equals(ApplicationConstant.ACL_MOBILE_PICKUP_CONFIRMATION)){
                    dataAdapterVO = new DataAdapterVO();
                    dataAdapterVO.setText("Pickup Confirmation");
                    dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_MAIN_PICKUP_CONFIRMATION);
                    dataAdapterVO.setImage(R.drawable.rof_mail);
                    datalist.add(dataAdapterVO);
                }





            }
        } catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }




























        return  datalist;
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*getforwarder();
        getServiceType();
        getShipmentType();
        getPaymentType();*/


    }

    public  void  getforwarder(){
        VolleyUtils.makeJsonObjectRequest(this, LastmileBO.getCache(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Forwarder" ,response, MainActivity.this);
                    return;
                }

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_FORWARDER, response.toString());
                edit.apply();
                edit.commit();


            }
        });
    }

    public  void  getDefaultValue(){
        VolleyUtils.makeJsonObjectRequest(this, DefaultValueBO.getDefaultValueCache(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

                Toast.makeText(MainActivity.this, "asfaf", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    Toast.makeText(MainActivity.this, "sdfafd", Toast.LENGTH_SHORT).show();
                    VolleyUtils.furnishErrorMsg(  "DefaultValue" ,response, MainActivity.this);
                    return;
                }
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_DEFAULTVALUE, response.getString("data"));
                edit.apply();
                edit.commit();
                setDefaultValue();

            }
        });

    }

    public  void  getServiceType(){
        VolleyUtils.makeJsonObjectRequest(this, ServiceTypeBO.getServiceTypeCache(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Service Type" ,response, MainActivity.this);
                    return;
                }

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_SERVICETYPE,response.toString());
                edit.apply();
                edit.commit();


            }
        });



    }


    public  void  getShipmentType(){
        VolleyUtils.makeJsonObjectRequest(this, ShipmentTypeBO.getShipmentType(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Service Type" ,response, MainActivity.this);
                    return;
                }

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_SHIPMENTTYPE,response.toString());
                edit.apply();
                edit.commit();


            }
        });
    }


    public void getPaymentType(){
        VolleyUtils.makeJsonObjectRequest(this, PaymentTypeBO.getPaymentType(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Payment Type" ,response, MainActivity.this);
                    return;
                }

                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_PAYMENTTYPE,response.toString());
                edit.apply();
                edit.commit();


            }
        });



    }


    public  void  setDefaultValue(){
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        String defaultvalue= sharedPreferences.getString( ApplicationConstant.CACHE_DEFAULTVALUE,null);
        String servicetype= sharedPreferences.getString( ApplicationConstant.CACHE_SERVICETYPE,null);
        String paymenttype= sharedPreferences.getString( ApplicationConstant.CACHE_PAYMENTTYPE,null);
        String shipmenttypecache= sharedPreferences.getString( ApplicationConstant.CACHE_SHIPMENTTYPE,null);
        try {



            JSONObject jsonObject = new JSONObject(defaultvalue);

            DefaultVO defaultVO=new DefaultVO();

            int defaultservicetypeid= (int) jsonObject.get("ServiceTypeId");
            int defaultpaymenttypeid=(int) jsonObject.get("PaymentTypeId");
            int defaultshipmenttypeid=(int) jsonObject.get("ShipmentTypeId");

            JSONObject service = new JSONObject(servicetype);
            JSONArray jsonArray= service.getJSONArray("dataList");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                if(jsonObject1.getInt("serviceTypeId")==defaultservicetypeid){
                    defaultVO.setServiceTypeName(jsonObject1.getString("name"));
                    defaultVO.setServiceTypeId(Integer.valueOf(jsonObject1.getString("serviceTypeId")));

                    break;
                }
            }

            JSONObject payment = new JSONObject(paymenttype);
            JSONArray paymentarry= payment.getJSONArray("dataList");
            for(int i=0;i<paymentarry.length();i++){
                JSONObject jsonObject1=paymentarry.getJSONObject(i);
                if(jsonObject1.getInt("paymentTypeId")==defaultpaymenttypeid){


                    defaultVO.setPaymentTypeName(jsonObject1.getString("typeName"));
                    defaultVO.setPaymentTypeId(Integer.valueOf(jsonObject1.getString("paymentTypeId")));

                    break;
                }
            }

            JSONObject shipmentdate = new JSONObject(shipmenttypecache);
            JSONArray shipmenttypearry= shipmentdate.getJSONArray("dataList");
            for(int i=0;i<shipmenttypearry.length();i++){
                JSONObject jsonObject1=shipmenttypearry.getJSONObject(i);
                if(jsonObject1.getInt("typeId")==defaultshipmenttypeid){
                    defaultVO.setShipmentTypeName(jsonObject1.getString("typeName"));
                    defaultVO.setShipmentTypeId(Integer.valueOf(jsonObject1.getString("typeId")));
                    break;
                }
            }


            SharedPreferences.Editor edit= sharedPreferences.edit();

            Gson gson = new Gson();
            String json = gson.toJson(defaultVO);
            edit.putString( ApplicationConstant.DEFAULTVALUE_BOOKING,json);
            edit.apply();
            edit.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
        }

    }

/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Log.e("denied", permission);
                Toast.makeText(MainActivity.this, "denied", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Log.e("allowed", permission);
                  //  Toast.makeText(myActivity, "allowed", Toast.LENGTH_SHORT).show();
                } else {
                    //set to never ask again
                    Log.e("set to never ask again", permission);
                    //Toast.makeText(myActivity, "set to never ask again", Toast.LENGTH_SHORT).show();
                    //do something here.
                }
            }
        }
    }
*/


}