package com.uav.rof.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uav.rof.R;
import com.uav.rof.bo.AwbnoBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.permissionhandler.PermissionHandler;
import com.uav.rof.util.Utility;
import com.uav.rof.vo.LastmileVO;
import com.uav.rof.vo.ServiceTypeVO;
import com.uav.rof.vo.ShipmentUnitVO;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class BookingReceived extends AppCompatActivity {
    EditText bookingawbno;
    boolean datavalid;
    Button pickupreceivesave;
    ImageView cameraofawbno,cameraforwardingawbno;
    int REQ_BARCODE=1001,REQ_DIALOG_SAVE=1002,REQ_SERVICETYPE=1003,REQ_BARCODE_FORWARDRE_AWB=1004;
    TextView viewlastno;

    EditText weight,servicename,serviceid,forwardingawbno,vendorforwarding,vendorforwardingid;

    LinearLayout forwarder_awb_layout, forwarder_layout;
    SharedPreferences sharedPreferences;
    String vendoridserver;
    String vendornameserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_receive);
        getSupportActionBar().hide();



        ImageView back_activity_button=findViewById(R.id.back_activity_button);
        cameraofawbno=findViewById(R.id.cameraofawbno);
        bookingawbno=findViewById(R.id.bookingawbno);
        viewlastno=findViewById(R.id.viewlastno);

        weight=findViewById(R.id.weight);
        servicename=findViewById(R.id.servicename);
        serviceid=findViewById(R.id.serviceid);
        forwardingawbno=findViewById(R.id.forwardingawbno);
        vendorforwarding=findViewById(R.id.vendorforwarding);
        vendorforwardingid=findViewById(R.id.vendorforwardingid);
        forwarder_awb_layout=findViewById(R.id.forwarder_awb_layout);
        forwarder_layout=findViewById(R.id.forwarder_layout);
        cameraforwardingawbno=findViewById(R.id.cameraforwardingawbno);

        forwarder_awb_layout.setVisibility(View.GONE);
        forwarder_layout.setVisibility(View.GONE);


        vendorforwarding.setEnabled(false);

        viewlastno.setVisibility(View.GONE);
        pickupreceivesave=findViewById(R.id.pickupreceivesave);

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        getVendorInCache();



        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookingawbno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AwbnoVerify();
                    return true;
                }
                return false;
            }
        });


        forwardingawbno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!forwardingawbno.getText().toString().equals("")) {
                        getForworderAwbNo();
                    }
                    return true;
                }
                return false;
            }
        });
        forwardingawbno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                    // forwardingawbno.setError(null);
                }
                //onBlur
                else {
                    forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                    if(!forwardingawbno.getText().toString().equals("")){

                        getForworderAwbNo();
                    }

                }
            }
        });



        forwardingawbno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                vendorforwarding.setText("");
                vendorforwardingid.setText("");
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0){
                    datavalid=true;
                }
            }
        });


        cameraforwardingawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PermissionHandler.cameraPermission(BookingReceived.this)){
                    return;
                }
                startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE_FORWARDRE_AWB);

            }
        });









        servicename.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    servicename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Service Type");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_SERVICETYPE);
                    connectionVO.setEntityIdKey("serviceTypeId");
                    connectionVO.setEntityTextKey("name");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_SERVICETYPE);
                }
                return true; // return is important...
            }
        });
        bookingawbno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    AwbnoVerify();
                }
            }
        });

        cameraofawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PermissionHandler.cameraPermission(BookingReceived.this)){
                    Toast.makeText(BookingReceived.this, "sddd", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE);

            }
        });


        pickupreceivesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;

                if(bookingawbno.getText().toString().equals("")){
                    bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                    valid=false;
                }else if(weight.getText().toString().equals("")){
                    weight.setError(Utility.getErrorSpannableString(getApplicationContext(), "Weight is Empty"));
                    valid=false;
                }else if(servicename.getText().toString().equals("") || serviceid.getText().toString().equals("")){
                    servicename.setError(Utility.getErrorSpannableString(getApplicationContext(), "Service Type is Empty"));
                    valid=false;
                }else if(!forwardingawbno.getText().toString().equals("") && vendorforwardingid.getText().toString().equals("")){
                    forwardingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Forwarding number against Forwarder not found"));
                    valid=false;
                }
                if(!valid ||!datavalid ){
                    return;
                }else {
                    SaveBookingreceived();
                }
            }
        });

    }


    public void getVendorInCache(){
         vendoridserver= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYID,null);
         vendornameserver= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYNAME,null);


        if(vendoridserver==null || vendornameserver==null){
            String memberId= sharedPreferences.getString(ApplicationConstant.CACHE_MAMBERID,null);

            VolleyUtils.makeJsonObjectRequest(this, AwbnoBO.getVendorByMemberId(Integer.parseInt(memberId)), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if(response.get("status").equals("fail")){
                        JSONArray error = response.getJSONArray("error");
                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<error.length(); i++){
                            sb.append(error.get(i)).append("\n");
                        }
                        Utility.alertDialog(BookingReceived.this,"Fail !",sb.toString(),"Ok");
                    }else {
                        JSONObject data=response.getJSONObject("dataList");
                        SharedPreferences.Editor edit= sharedPreferences.edit();
                        edit.putString( ApplicationConstant.CACHE_ATTORNEYID, data.getString("attorneyId"));
                        edit.putString( ApplicationConstant.CACHE_ATTORNEYNAME, data.getString("attorneyName"));
                        edit.apply();
                        edit.commit();
                    }

                }
            });
        }
    }




    public void getForworderAwbNo(){

        String memberId= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYID,null);
        VolleyUtils.makeJsonObjectRequest(this, AwbnoBO.getAWBByAttorney(forwardingawbno.getText().toString(),Integer.parseInt(memberId)), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;


                if(response.getString("status").equals("fail")){
                    JSONArray error = response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }


                    forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                    forwardingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(),  sb.toString()));


                    datavalid =false;

                }else {
                    JSONObject response1 = response.getJSONObject("dataList");

                    Log.w("resp",response1.toString());
                    forwardingawbno.setError(null);

                    if(response1.getString("statusId").equals("Active")){

                        forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                        forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                        vendorforwarding.setText(response1.getString("name"));
                        vendorforwardingid.setText(response1.getString("lastMileId"));
                        datavalid =true;


                    }else {

                        forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                        forwardingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "This No. already use"));
                        vendorforwarding.setText("");
                        vendorforwardingid.setText("");
                        datavalid =false;
                    }
                }
            }
        });
    }




    public void SaveBookingreceived(){


        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String memberid = (String) sharedPreferences.getString(ApplicationConstant.CACHE_MAMBERID, null);

        ConnectionVO  connectionVO =AwbnoBO.vendorBookingPickup();

        HashMap<String, Object> params = new HashMap<String, Object>();
        ShipmentUnitVO shipmentUnitVO =new ShipmentUnitVO();

        ServiceTypeVO serviceTypeVO =new ServiceTypeVO();
        serviceTypeVO.setServiceTypeId(Integer.parseInt(serviceid.getText().toString()));

        LastmileVO forwarder =new LastmileVO();


        if(forwarder_awb_layout.getVisibility() == View.VISIBLE && forwarder_layout.getVisibility() == View.VISIBLE && !forwardingawbno.getText().toString().equals("") && !vendorforwardingid.getText().toString().equals("")){
            shipmentUnitVO.setFwdNumber(forwardingawbno.getText().toString());
            forwarder.setLastMileId(Integer.parseInt(vendorforwardingid.getText().toString()));
            shipmentUnitVO.setForwarder(forwarder);
        }




        shipmentUnitVO.setAwbNo(bookingawbno.getText().toString());
        shipmentUnitVO.setAnonymousInteger(Integer.parseInt(memberid));
        shipmentUnitVO.setShipmentWeight(Double.parseDouble(weight.getText().toString()));
        shipmentUnitVO.setServicetype(serviceTypeVO);

        Gson gson = new Gson();
        String json = gson.toJson(shipmentUnitVO);


        Log.w(getApplication().getPackageName(),json);
        params.put("shipment",json);

        connectionVO.setParams(params);






        VolleyUtils.makeJsonObjectRequest(this,connectionVO, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                if(response.get("status").equals("fail")){
                    AwbnoVerify();
                    VolleyUtils.furnishErrorMsg(  "Error " ,response, BookingReceived.this);
                    datavalid =false;
                }else {



                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(BookingReceived.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Shipment Successfully  Save");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            weight.setText("");
                            servicename.setText("");
                            serviceid.setText("");

                            viewlastno.setVisibility(View.VISIBLE);
                            String text = "<font color=#cc0029>Last Number :- </font>"+bookingawbno.getText().toString();
                            viewlastno.setText(Html.fromHtml(text));
                            bookingawbno.setText("");
                            datavalid=false;
                            bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);

                        }
                    });
                    alertDialog.show();





                }

            }
        });
    }







    public void AwbnoVerify(){
        VolleyUtils.makeJsonObjectRequest(this, AwbnoBO.getawbVerifyToPickup(bookingawbno.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;


                Log.w("resp",response.toString());
                bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                if(response.get("status").equals("fail")){
                    viewlastno.setVisibility(View.GONE);
                    viewlastno.setText("");

                    JSONArray error = response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(),  sb.toString()));
                    weight.setText("");
                    servicename.setText("");
                    serviceid.setText("");


                    datavalid =false;
                }else {

                    if(response.get("status").equals("success")){

                        JSONObject object=response.getJSONObject("shipmentvalues");
                        viewlastno.setVisibility(View.VISIBLE);

                        DecimalFormat df = new DecimalFormat("0.000");

                        weight.setText(df.format(Double.parseDouble(object.getString("Weight"))));
                        servicename.setText(object.getString("ServiceTypename"));
                        serviceid.setText(object.getString("ServiceTypeId"));

                        bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);


                        if(!object.has("ForwardingAwbNo") && !object.has("ForwardingId") && !object.has("ForwardingName")){
                            forwarder_awb_layout.setVisibility(View.VISIBLE);
                            forwarder_layout.setVisibility(View.VISIBLE);
                        }else {
                            forwarder_awb_layout.setVisibility(View.GONE);
                            forwarder_layout.setVisibility(View.GONE);
                        }
                        datavalid =true;
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_BARCODE) {
                bookingawbno.setText(data.getStringExtra("key"));
                AwbnoVerify();
            }

            if (requestCode == REQ_SERVICETYPE) {
                servicename.setText(data.getStringExtra("valueName"));
                serviceid.setText(data.getStringExtra("valueId"));


            }

            if(requestCode==REQ_BARCODE_FORWARDRE_AWB){
                forwardingawbno.setText(data.getStringExtra("key"));
                getForworderAwbNo();
            }

        }
    }


}
