package com.uav.rof.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uav.exceptions.ExceptionsNotification;
import com.uav.rof.R;
import com.uav.rof.bo.AwbnoBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.bo.ShipmentSaveBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.permissionhandler.PermissionHandler;
import com.uav.rof.util.Utility;
import com.uav.rof.vo.AttorneyVO;
import com.uav.rof.vo.CityVO;
import com.uav.rof.vo.LastmileVO;
import com.uav.rof.vo.ServiceTypeVO;
import com.uav.rof.vo.VendorBookingVO;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class VendorBooking extends AppCompatActivity implements ImageTextViewInterface{
    EditText bookingdate,vendorname,vendorid,bookingawbno,
            pincode,cityname,cityid,servicetypename,servicetypeid,cneename,
            pcs,weight,forwardingawbno,vendorforwarding,vendorforwardingid;
    ImageView addressimage,back_activity_button;
    ImageButton cameraofaddress,cameraofawbno,cameraforwardingawbno;
    Button saveshipment;
    int REQ_BARCODE=1001,REQ_SERVICETYPE=1002,REQ_DIALOG_RESPONCE=1003,REQ_BARCODE_FORWARDRE_AWB=1004,REQ_IMAGEADDRESS = 1005,REQ_WITHOUTPREVIEW =1006;;
    Boolean datavalid=true;
    SharedPreferences sharedPreferences;


    private Uri mImageUri;
    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_booking);
        ImageView back_activity_button=findViewById(R.id.back_activity_button);
        getSupportActionBar().hide();

        bookingdate=findViewById(R.id.bookingdate);
        vendorname=findViewById(R.id.vendorname);
        vendorid=findViewById(R.id.vendorid);
        bookingawbno=findViewById(R.id.bookingawbno);
        cameraofawbno=findViewById(R.id.cameraofawbno);
        pincode=findViewById(R.id.pincode);
        cityname=findViewById(R.id.cityname);
        cityid=findViewById(R.id.cityid);
        servicetypename=findViewById(R.id.servicename);
        servicetypeid=findViewById(R.id.serviceid);
        cneename=findViewById(R.id.cneename);

        addressimage=findViewById(R.id.addressimage);
        cameraofaddress=findViewById(R.id.cameraofaddress);

        forwardingawbno=findViewById(R.id.forwardingawbno);
        vendorforwarding=findViewById(R.id.vendorforwarding);
        vendorforwardingid=findViewById(R.id.vendorforwardingid);

        cameraforwardingawbno=findViewById(R.id.cameraforwardingawbno);


        pcs=findViewById(R.id.pcs);
        weight=findViewById(R.id.weight);
        saveshipment=findViewById(R.id.saveshipment);



        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);

        vendorforwarding.setEnabled(false);

        String vendoridserver= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYID,null);
        String vendornameserver= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYNAME,null);


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
                        Utility.alertDialog(VendorBooking.this,"Fail !",sb.toString(),"Ok");
                    }else {
                        JSONObject data=response.getJSONObject("dataList");
                        vendorname.setText(data.getString("attorneyName"));
                        vendorid.setText(data.getString("attorneyId"));
                        SharedPreferences.Editor edit= sharedPreferences.edit();
                        edit.putString( ApplicationConstant.CACHE_ATTORNEYID, data.getString("attorneyId"));
                        edit.putString( ApplicationConstant.CACHE_ATTORNEYNAME, data.getString("attorneyName"));
                        edit.apply();
                        edit.commit();
                    }

                }
            });
        }else{
            vendorname.setText(vendornameserver);
            vendorid.setText(vendoridserver);
        }
        cityname.setEnabled(false);
        vendorname.setEnabled(false);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        bookingdate.setText(dateFormat.format(new Date()));
        bookingdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(VendorBooking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                bookingdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" +year);
                                bookingdate.setError(null);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
           }
        });


        addressimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorBooking.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, mImageUri.toString());
                startActivity(intent);
            }
        });

        cameraofaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(VendorBooking.this)){
                    return;
                }
                if(!PermissionHandler.readWritePermission(VendorBooking.this)){
                    return;
                }
                String data= (String)sharedPreferences.getString(ApplicationConstant.CACHE_CAMERA_PREVIEW ,null);
                if(data==null || data=="true"){
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo;
                    try
                    {
                        // place where to store camera taken picture
                        photo = VolleyUtils.createTemporaryFile("picture", ".jpg");
                        photo.delete();
                        mImageUri = Uri.fromFile(photo);
                        /*Uri mImageUri = CustomProvider.getPhotoUri(photo);
                         */
                        Uri mImageUri = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()
                                + ".provider", photo);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        //start camera intent
                        startActivityForResult(intent, REQ_IMAGEADDRESS);
                    }
                    catch(Exception e)
                    {
                        Log.d("Exception",e.getMessage());
                        return;
                    }
                }else{
                    Intent cameraactivity = new Intent(getApplicationContext(),Click_Image_Without_Preview.class);
                    startActivityForResult(cameraactivity,REQ_WITHOUTPREVIEW);
                }
            }
        });




        forwardingawbno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!forwardingawbno.getText().toString().equals("")) {
                        AwbnoVerify("fawb", forwardingawbno.getText().toString());
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

                        AwbnoVerify("fawb",forwardingawbno.getText().toString());
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
            }
        });





        bookingawbno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!bookingawbno.getText().toString().equals("")) {

                        AwbnoVerify("awb", bookingawbno.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        bookingawbno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                   // bookingawbno.setError(null);
                }
                //onBlur
                else {
                    AwbnoVerify("awb",bookingawbno.getText().toString());
                }
            }
        });

        cameraofawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PermissionHandler.cameraPermission(VendorBooking.this)){
                    Toast.makeText(VendorBooking.this, "sddd", Toast.LENGTH_SHORT).show();
                    return;
                }
                 startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE);

            }
        });

        cameraforwardingawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PermissionHandler.cameraPermission(VendorBooking.this)){
                    return;
                }
                startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE_FORWARDRE_AWB);

            }
        });







        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //set the row background to a different color
                }
                //onBlur
                else {
                    if (pincode.length() < 6) {

                        pincode.setError(Utility.getErrorSpannableString(getApplicationContext(),  "Plz enter at least 6 characters"));
                        cityname.setText("");
                    }
                }
            }
        });

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pincode.length() == 6) {
                    pincodebycity();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        servicetypename.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    servicetypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
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


        saveshipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyshipmentunitandsave();
            }
        });



    }
        public void AwbnoVerify(final String type, String awbno){

        String memberId= sharedPreferences.getString(ApplicationConstant.CACHE_ATTORNEYID,null);
        VolleyUtils.makeJsonObjectRequest(this, AwbnoBO.getAWBByAttorney(awbno,Integer.parseInt(memberId)), new VolleyResponseListener() {
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

                    if(type.equals("awb")){
                        bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                        bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(),  sb.toString()));
                    }
                    if(type.equals("fawb")){
                        forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                        forwardingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(),  sb.toString()));
                    }

                    datavalid =false;

                }else {
                    JSONObject response1 = response.getJSONObject("dataList");

                    Log.w("resp",response1.toString());

                    if(response1.getString("statusId").equals("Active")){


                        if(type.equals("awb")){

                            bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);

                            bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                            pincode.requestFocus();

                        }
                        if(type.equals("fawb")){

                            forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);

                            forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                            vendorforwarding.setText(response1.getString("name"));
                            vendorforwardingid.setText(response1.getString("lastMileId"));

                        }

                        datavalid =true;


                    }else {

                        if(type.equals("awb")){
                            bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                            bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "This No. already use"));
                            pincode.requestFocus();

                        }
                        if(type.equals("fawb")){
                            forwardingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                            forwardingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "This No. already use"));
                            vendorforwarding.setText("");
                            vendorforwardingid.setText("");

                        }

                        datavalid =false;
                    }
                }
            }
        });
    }



    public void pincodebycity() {
        pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
        VolleyUtils.makeJsonObjectRequest(this, PincodeBO.getPincode(pincode.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    pincode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Pincode not valid"));
                    datavalid =false;

                }else {
                    pincode.setError(null);
                    datavalid =true;
                    Pincodebycityresponce(response.toString());

                }

            }
        });
    }


    public  void  Pincodebycityresponce(String resp){
            JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(resp.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("dataList");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("city");
            cityname.setText(jsonObject2.getString("cityName"));
            cityid.setText(jsonObject2.getString("cityId"));
            pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
            servicetypename.requestFocus();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_BARCODE) {
                bookingawbno.setText(data.getStringExtra("key"));
                AwbnoVerify("awb",bookingawbno.getText().toString());
            }
            if (requestCode == REQ_SERVICETYPE) {
                servicetypename.setText(data.getStringExtra("valueName"));
                servicetypeid.setText(data.getStringExtra("valueId"));

                cneename.requestFocus();
                servicetypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);

            }

            if(requestCode==REQ_BARCODE_FORWARDRE_AWB){
                forwardingawbno.setText(data.getStringExtra("key"));
                AwbnoVerify("fawb",forwardingawbno.getText().toString());
            }


            if(requestCode==REQ_WITHOUTPREVIEW){
                try {
                    bmp = Utility.getImageFileFromSDCared(new File(data.getStringExtra("imagepath")));


                    if (bmp.getWidth() > bmp.getHeight()) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    }
                    addressimage.setImageBitmap(bmp);
                    addressimage.setVisibility(View.VISIBLE);
                    mImageUri = Uri.fromFile(new File(data.getStringExtra("imagepath")));
                    pincode.setText("");
                    pincode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    ImageTextApi b = new ImageTextApi(this, VendorBooking.this, bmp);
                    b.getPincode();
                }catch (Exception e){
                    Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();
                    ExceptionsNotification.ExceptionHandling(VendorBooking.this,Utility.getStackTrace(e));
                }
            }


            if (requestCode == REQ_IMAGEADDRESS) {
                try{
                    bmp =VolleyUtils.grabImage(mImageUri,VendorBooking.this);
                    if(bmp.getWidth()>bmp.getHeight()){
                        Matrix matrix =new Matrix();
                        matrix.postRotate(90);
                        bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                    }
                    addressimage.setImageBitmap(bmp);
                    addressimage.setVisibility(View.VISIBLE);
                    pincode.setText("");
                    pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);

                    ImageTextApi b = new ImageTextApi(this, VendorBooking.this, bmp);
                    b.getPincode();

                }catch (Exception e){
                    Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();


                    ExceptionsNotification.ExceptionHandling(VendorBooking.this,Utility.getStackTrace(e));
                }

            }







        }
    }


    public  void  verifyshipmentunitandsave(){

        Boolean valid;

        valid=true;
        if(bookingdate.getText().toString().equals("")){
            bookingdate.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }

        if(bookingawbno.getText().toString().equals("")){
            bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }
        if(pincode.getText().toString().equals("")){
            pincode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }
        if(servicetypename.getText().toString().equals("")){
            servicetypename.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }
        if(weight.getText().toString().equals("")){
            weight.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }
        if(pcs.getText().toString().equals("")){
            pcs.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }



        if(cneename.getText().toString().equals("")){
            cneename.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
            valid=false;
        }

        if(!forwardingawbno.getText().toString().equals("") && vendorforwardingid.getText().toString().equals("") || forwardingawbno.getText().toString().equals("") && !vendorforwardingid.getText().toString().equals("")){
            Utility.alertDialog(VendorBooking.this,"Alert","Forwarding awb and Forwarder both are required","Ok");
            valid=false;
        }

        if(forwardingawbno.getText().toString().equals(bookingawbno.getText().toString())){
            Utility.alertDialog(VendorBooking.this,"Alert","Both are same Awb no and Forwarding Awn no","Ok");
            valid=false;
        }




       if(!valid || !datavalid){
            return;
        }else {

           try {
               saveVendorBooking();
           } catch (Exception e) {
               Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();
               ExceptionsNotification.ExceptionHandling(VendorBooking.this,Utility.getStackTrace(e));
           }
       }
    }



    public void saveVendorBooking() throws ParseException {

        try{


        HashMap<String, Object> params = new HashMap<String, Object>();
        ConnectionVO connectionVO = ShipmentSaveBO.saveBooking();
        VendorBookingVO vendorBookingVO=new VendorBookingVO();
        CityVO cityVO = new CityVO();
        cityVO.setCityId(Integer.parseInt(cityid.getText().toString()));

        LastmileVO lastmileVO =new LastmileVO();
        if(!vendorforwardingid.getText().toString().equals("")){
            lastmileVO.setLastMileId(Integer.parseInt(vendorforwardingid.getText().toString()));
            vendorBookingVO.setForwarder(lastmileVO);
        }





        ServiceTypeVO serviceTypeVO = new ServiceTypeVO();
        serviceTypeVO.setServiceTypeId(Integer.parseInt(servicetypeid.getText().toString()));
        AttorneyVO attorneyVO = new AttorneyVO();
        attorneyVO.setAttorneyID(Integer.parseInt(vendorid.getText().toString()));
        vendorBookingVO.setCity(cityVO);
        vendorBookingVO.setServicetype(serviceTypeVO);
        vendorBookingVO.setAwbNo(bookingawbno.getText().toString());
        Date bookingDate = new SimpleDateFormat("dd-MM-yyyy").parse(bookingdate.getText().toString());
        vendorBookingVO.setBookingDate(bookingDate.getTime());
        vendorBookingVO.setAttorney(attorneyVO);
        vendorBookingVO.setPincode(pincode.getText().toString());
        vendorBookingVO.setShipmentWeight(Double.parseDouble(weight.getText().toString()));
        vendorBookingVO.setPcs(Integer.parseInt(pcs.getText().toString()));
        vendorBookingVO.setConsigneeName(cneename.getText().toString());
        vendorBookingVO.setForwarderAwbNo(forwardingawbno.getText().toString());
        if(mImageUri!=null){
            vendorBookingVO.setImage(Utility.imagetostring(mImageUri,this));
        }
        Gson gson = new Gson();
        String json = gson.toJson(vendorBookingVO);
        params.put("booking", json);

        connectionVO.setParams(params);




            VolleyUtils.makeJsonObjectRequest(this, connectionVO, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if (response.get("status").equals("fail")) {
                        VolleyUtils.furnishErrorMsg("ShipmentUnit Save", response, VendorBooking.this);
                        return;
                    } else {

                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(VendorBooking.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Shipment Successfully  Save");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(new Intent(VendorBooking.this,VendorBooking.class));
                            }
                        });
                        alertDialog.show();


                    }
                }
            });


        }catch (Exception e){
            Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();
            ExceptionsNotification.ExceptionHandling(VendorBooking.this,Utility.getStackTrace(e));
        }

    }

    @Override
    public void onResult(final String o) {

        Handler runOnUiThread;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(o!=null ){
                    pincode.setText(o);
                }else {
                    Toast.makeText(VendorBooking.this, "Pincode Con't be read ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
