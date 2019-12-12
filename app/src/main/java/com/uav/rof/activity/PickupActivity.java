package com.uav.rof.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uav.exceptions.ExceptionsNotification;
import com.uav.rof.R;
import com.uav.rof.bo.AwbnoBO;
import com.uav.rof.bo.CustomerBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.bo.SaveImageBO;
import com.uav.rof.bo.SavePickupBO;
import com.uav.rof.bo.ShipmentSaveBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.permissionhandler.PermissionHandler;
import com.uav.rof.util.Utility;
import com.uav.rof.vo.CityVO;
import com.uav.rof.vo.CustomerVO;
import com.uav.rof.vo.FlieVO;
import com.uav.rof.vo.LastmileVO;
import com.uav.rof.vo.PaymentTypeVO;
import com.uav.rof.vo.PickupVO;
import com.uav.rof.vo.ServiceTypeVO;
import com.uav.rof.vo.ShipmentModeVO;
import com.uav.rof.vo.ShipmentTypeVO;
import com.uav.rof.vo.ShipmentUnitVO;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PickupActivity extends AppCompatActivity {






    private static int REQ_CUSTOMERAUTOSEARCH = 1002;
    private static int REQ_BARCODE = 1003;
    private static int REQ_IMAGEADDRESS = 1004;
    private static int REQ_DIALOG = 1008;
    private static int REQ_DIALOG_RESPONCE = 1009;
    private static int REQ_WITHOUTPREVIEW=1010;

    private Uri mImageUri;
    Boolean datavalid=true;


    SharedPreferences sharedPreferences;
    EditText customername, customerid, bookingawbno, address,pincode, cityname, cityid;



    ImageButton cameraofaddress,cameraofawbno;
    ImageView addressimage,back_activity_button;
    ProgressBar progressBar;
    HashMap<String, Object> params;
    Bitmap bmp;

    Button saveshipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);

        customername = (EditText) findViewById(R.id.customername);
        customerid = (EditText) findViewById(R.id.customerid);
        bookingawbno = (EditText) findViewById(R.id.bookingawbno);
        cameraofaddress = (ImageButton) findViewById(R.id.cameraofaddress);
        addressimage=(ImageView)findViewById(R.id.addressimage);
        saveshipment=(Button)findViewById(R.id.saveshipment);
        back_activity_button= findViewById(R.id.back_activity_button);
        cameraofawbno = (ImageButton) findViewById(R.id.cameraofawbno);


        pincode = (EditText) findViewById(R.id.pincode);
        cityname = (EditText) findViewById(R.id.cityname);
        cityid = (EditText) findViewById(R.id.cityid);

        cityname.setEnabled(false);

        getSupportActionBar().hide();

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        // Keyboard enter  event
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







        customername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    ConnectionVO connectionVO = CustomerBO.getCustomerSearch();
                    Intent intent = new Intent(getApplicationContext(),AutoSearchView.class);
                    intent.putExtra( ApplicationConstant.INTENT_EXTRA_CONNECTION , connectionVO);
                    startActivityForResult(intent,REQ_CUSTOMERAUTOSEARCH);
                }
                return true; // return is important...
            }
        });





        cameraofawbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(PickupActivity.this)){
                    Toast.makeText(PickupActivity.this, "", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE);
            }
        });




        addressimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickupActivity.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, mImageUri.toString());
                startActivity(intent);
            }
        });

        saveshipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyshipmentunitandsave();
            }
        });

        cameraofaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionHandler.cameraPermission(PickupActivity.this)){
                    return;
                }
                if(!PermissionHandler.readWritePermission(PickupActivity.this)){
                    return;
                }


                String data= (String)sharedPreferences.getString(ApplicationConstant.CACHE_CAMERA_PREVIEW ,null);


                if(data==null || data=="true") {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo;
                    try {
                        // place where to store camera taken picture
                        photo = VolleyUtils.createTemporaryFile("picture", ".jpg");
                        photo.delete();
                        mImageUri = Uri.fromFile(photo);

                        Uri mImageUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                                + ".provider", photo);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, REQ_IMAGEADDRESS);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                        ExceptionsNotification.ExceptionHandling(PickupActivity.this , Utility.getStackTrace(e));
                        return;
                    }
                }else {
                    Intent cameraactivity = new Intent(getApplicationContext(),Click_Image_Without_Preview.class);
                    startActivityForResult(cameraactivity,REQ_WITHOUTPREVIEW);
                }
            }
        });

    }






    public  void  verifyshipmentunitandsave(){

        Boolean valid;
        valid=true;
        if(customername.getText().toString().equals("")){
            customername.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
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

        if(pincode.getText().length()<6){
            pincode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Pincode is wrong"));
            valid=false;
        }


        if(addressimage.getDrawable() == null){
            Utility.alertDialog(PickupActivity.this,"Alert","Address is Empty ","Ok");
            valid=false;
        }
        if(!valid || !datavalid){
            return;
        }else {
            saveimageserver();
        }
    }


    public  void saveimageserver(){
        HashMap<String , Object>  params = new HashMap<String, Object>();
        ConnectionVO connectionVO = SaveImageBO.getImage();
        if(addressimage.getDrawable() != null){
            params.put("file",Utility.imagetostring(mImageUri,PickupActivity.this));
        }
        params.put("awbno",bookingawbno.getText().toString());
        params.put("extension","jpg");
        connectionVO.setParams(params);
        VolleyUtils.makeJsonObjectRequest(this, connectionVO, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;

                if(response.get("status").equals("fail")){
                    VolleyUtils.furnishErrorMsg(  "Image Save" ,response, PickupActivity.this);
                    return;
                }else {
                    Log.w("responce",""+response.get("id"));
                    SaveShipmentUnit((Integer) response.get("id"));
                }
            }
        });
    }






    public void SaveShipmentUnit(int id) throws JSONException {

        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            ConnectionVO connectionVO = SavePickupBO.getSavePickup();

            CustomerVO customerVO = new CustomerVO();
            customerVO.setCustomerId(Integer.parseInt(customerid.getText().toString()));

            FlieVO flieVO =new FlieVO();

            flieVO.setFileId(id);

            PickupVO pickupVO = new PickupVO();
            pickupVO.setAwbNo(bookingawbno.getText().toString());


            pickupVO.setCustomer(customerVO);
            pickupVO.setFileId(flieVO);
            pickupVO.setPincode(pincode.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(pickupVO);
            params.put("shipment", json);
            connectionVO.setParams(params);
            VolleyUtils.makeJsonObjectRequest(this, connectionVO, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }

                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if (response.get("status").equals("fail")) {
                        VolleyUtils.furnishErrorMsg("Pickup ", response, PickupActivity.this);
                        return;
                    } else {
                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(PickupActivity.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Shipment Successfully  Save");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(new Intent(PickupActivity.this,PickupActivity.class));
                             }
                        });
                        alertDialog.show();

                    }
                }
            });

        }catch(Exception e){
             ExceptionsNotification.ExceptionHandling(PickupActivity.this , Utility.getStackTrace(e));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CUSTOMERAUTOSEARCH) {
                customersearchresp(data);
            }
            if (requestCode == REQ_BARCODE) {
                bookingawbno.setText(data.getStringExtra("key"));
                AwbnoVerify();
            }




            if (requestCode == REQ_IMAGEADDRESS) {
                try{
                    bmp =VolleyUtils.grabImage(mImageUri,PickupActivity.this);
                    if(bmp.getWidth()>bmp.getHeight()){
                        Matrix matrix =new Matrix();
                        matrix.postRotate(90);
                        bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                    }
                    addressimage.setImageBitmap(bmp);
                    addressimage.setVisibility(View.VISIBLE);
                    pincode.setText("");
                    pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);


                    if(Utility.getImageByPincode(PickupActivity.this,bmp)!=null){
                        pincode.setText(Utility.getImageByPincode(PickupActivity.this,bmp));
                    };


                }catch (Exception e){
                    Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();
                    ExceptionsNotification.ExceptionHandling(PickupActivity.this,Utility.getStackTrace(e));
                }
            }
            if(requestCode==REQ_WITHOUTPREVIEW){

                bmp=Utility.getImageFileFromSDCared(new File(data.getStringExtra("imagepath")));
                if(bmp.getWidth()>bmp.getHeight()){
                    Matrix matrix =new Matrix();
                    matrix.postRotate(90);
                    bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                }
                addressimage.setImageBitmap(bmp);
                addressimage.setVisibility(View.VISIBLE);
                mImageUri = Uri.fromFile(new File(data.getStringExtra("imagepath")));
                pincode.setText("");
                pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                if(Utility.getImageByPincode(PickupActivity.this,bmp)!=null){
                    pincode.setText(Utility.getImageByPincode(PickupActivity.this,bmp));
                };
            }
        }
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
                    VolleyUtils.furnishErrorMsg(  "Pincode" ,response, PickupActivity.this);
                    pincode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Pincode not valid"));
                    datavalid=false;
                }else {
                    pincode.setError(null);
                    datavalid=true;
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
        } catch (JSONException e) {
            e.printStackTrace();
            ExceptionsNotification.ExceptionHandling(PickupActivity.this , Utility.getStackTrace(e));
        }
    }





    public void customersearchresp(Intent intent){
        customername.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
        customername.setText(intent.getStringExtra("text"));
        customerid.setText(intent.getStringExtra("id"));
        bookingawbno.requestFocus();


        SharedPreferences.Editor edit1= sharedPreferences.edit();
        edit1.putString("customername",customername.getText().toString());
        edit1.putInt("customerid", Integer.valueOf(customerid.getText().toString()));
        edit1.apply();
        edit1.commit();
    }



    public void AwbnoVerify(){

        VolleyUtils.makeJsonObjectRequest(this, AwbnoBO.getawbnoverify(bookingawbno.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;


                bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                if(response.get("status").equals("fail")){
                    JSONArray error = response.getJSONArray("error");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<error.length(); i++){
                        sb.append(error.get(i)).append("\n");
                    }
                    bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(),  sb.toString()));
                    datavalid=false;
                }else {
                    JSONObject response1 = response.getJSONObject("dataList");
                    JSONObject response2 = response1.getJSONObject("lastmile");
                    if(response2.getString("statusId").equals("Active")){
                        bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                        datavalid=true;
                        pincode.requestFocus();
                    }else {
                        bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "This No. already use"));
                        datavalid=false;
                    }
                }

            }
        });

    }



    protected void onStart()
    {
        super.onStart();
        setcustomer();
    }

    public void setcustomer(){

        SharedPreferences sharedPreferences1= getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String name= sharedPreferences1.getString("customername",null);
        int nameid= sharedPreferences1.getInt("customerid",0);

        if(name!=null && nameid!=0 ){
            customername.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
            customername.setText(name);
            customerid.setText(""+nameid);
            customername.setSelection(customername.getText().length());

        }

    }

}
