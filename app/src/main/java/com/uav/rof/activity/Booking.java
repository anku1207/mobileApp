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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uav.exceptions.ExceptionsNotification;
import com.uav.rof.R;
import com.uav.rof.bo.AwbnoBO;
import com.uav.rof.bo.CustomerBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.bo.SaveImageBO;
import com.uav.rof.bo.ShipmentSaveBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.permissionhandler.PermissionHandler;
import com.uav.rof.util.Utility;
import com.uav.rof.vo.CityVO;
import com.uav.rof.vo.CustomerVO;
import com.uav.rof.vo.DefaultVO;
import com.uav.rof.vo.LastmileVO;
import com.uav.rof.vo.PaymentTypeVO;
import com.uav.rof.vo.ServiceTypeVO;
import com.uav.rof.vo.ShipmentModeVO;
import com.uav.rof.vo.ShipmentTypeVO;
import com.uav.rof.vo.ShipmentUnitVO;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Booking extends AppCompatActivity implements ImageTextViewInterface {


    private static int REQ_PINCODE = 1001;
    private static int REQ_CUSTOMERAUTOSEARCH = 1002;
    private static int REQ_BARCODE = 1003;

    private static int REQ_IMAGEADDRESS = 1004;
    private static int REQ_SERVICETYPE = 1005;
    private static int REQ_SHIPMENTTYPE = 1006;
    private static int REQ_PAYMENTTYPE = 1007;

    private static int REQ_DIALOG = 1008;
    private static int REQ_DIALOG_RESPONCE = 1009;
    private static int REQ_WITHOUTPREVIEW =1010;




    private Uri mImageUri;




    SharedPreferences sharedPreferences;

    EditText customername, customerid, bookingawbno, pincode, cityname, cityid, address, forwardername, forwarderid,servicetypename,servicetypeid,
            pcs,shipmenttype,shipmenttypeid,weight,paymenttypename,paymenttypeid,invoicevalue,cnee,declaredvalue,bookingdate;

    LinearLayout invoicelayout,declardlayout,saveshipmenticontop;

    ImageButton cameraofaddress,cameraofawbno;
    ImageView addressimage,back_activity_button;
    ProgressBar progressBar;
    HashMap<String, Object> params;
    Bitmap bmp;

    Button saveshipment;
    Boolean datavalid=true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        customername = (EditText) findViewById(R.id.customername);
        customerid = (EditText) findViewById(R.id.customerid);
        bookingawbno = (EditText) findViewById(R.id.bookingawbno);
        pincode = (EditText) findViewById(R.id.pincode);
        cityname = (EditText) findViewById(R.id.cityname);
        cityid = (EditText) findViewById(R.id.cityid);
       // address = (EditText) findViewById(R.id.address);
        forwardername = (EditText) findViewById(R.id.forwardername);
        forwarderid = (EditText) findViewById(R.id.forwarderid);
        servicetypename=(EditText)findViewById(R.id.servicename);
        servicetypeid=(EditText)findViewById(R.id.serviceid);
        cameraofaddress = (ImageButton) findViewById(R.id.cameraofaddress);
        cameraofawbno = (ImageButton) findViewById(R.id.cameraofawbno);
        addressimage=(ImageView)findViewById(R.id.addressimage);
        pcs=(EditText)findViewById(R.id.pcs);
        saveshipment=(Button)findViewById(R.id.saveshipment);
        shipmenttype=(EditText)findViewById(R.id.shipmenttype);
        shipmenttypeid=(EditText)findViewById(R.id.shipmenttypeid);
        weight=(EditText)findViewById(R.id.weight);
        paymenttypename=(EditText)findViewById(R.id.paymentname);
        paymenttypeid=(EditText)findViewById(R.id.paymenttypeid);
        invoicevalue=(EditText)findViewById(R.id.invoice);
        cnee=(EditText)findViewById(R.id.cnee);
        declaredvalue=(EditText)findViewById(R.id.declaredvalue);
        back_activity_button=(ImageView)findViewById(R.id.back_activity_button);
        bookingdate=(EditText)findViewById(R.id.bookingdate);
        saveshipmenticontop=(LinearLayout)findViewById(R.id.saveshipmenticonintop);


        /*StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
*/

        try{
            Defaultvalueset();
        }catch (Exception e){
            Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();
            ExceptionsNotification.ExceptionHandling(Booking.this,Utility.getStackTrace(e));
        }


        declardlayout=(LinearLayout)findViewById(R.id.declaredlayout);
        invoicelayout=(LinearLayout)findViewById(R.id.invoicelayout);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        params = new HashMap<>();
        cityname.setEnabled(false);
        forwardername.setEnabled(false);
        getSupportActionBar().hide();


      /*  getSupportActionBar().setTitle("Shipment Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //set booking current date

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(Booking.this,
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

        shipmenttype.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    shipmenttype.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Shipment Type");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_SHIPMENTTYPE);
                    connectionVO.setEntityIdKey("typeId");
                    connectionVO.setEntityTextKey("typeName");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_SHIPMENTTYPE);
                }
                return true; // return is important...
            }
        });

        paymenttypename.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {
                    paymenttypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
                    ConnectionVO connectionVO = new ConnectionVO();
                    connectionVO.setTitle("Payment Type");
                    connectionVO.setSharedPreferenceKey(ApplicationConstant.CACHE_PAYMENTTYPE);
                    connectionVO.setEntityIdKey("paymentTypeId");
                    connectionVO.setEntityTextKey("typeName");
                    Intent intent = new Intent(getApplicationContext(),ListViewSingleText.class);
                    intent.putExtra(ApplicationConstant.INTENT_EXTRA_CONNECTION,  connectionVO);
                    startActivityForResult(intent,REQ_PAYMENTTYPE);
                }
                return true; // return is important...
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

                if(!PermissionHandler.cameraPermission(Booking.this)){
                    Toast.makeText(Booking.this, "sddd", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivityForResult(new Intent(getApplicationContext(),BarcodeScanner.class),REQ_BARCODE);

            }
        });
        cameraofaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(  Booking.this , new String[]{android.Manifest.permission.CAMERA}, 50);
                }else {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQ_IMAGEADDRESS);
                }*/


               /* ActivityCompat.requestPermissions(  Booking.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 51);
                ActivityCompat.requestPermissions(  Booking.this , new String[]{android.Manifest.permission.CAMERA}, 50);*/


               if(!PermissionHandler.cameraPermission(Booking.this)){
                   return;
               }


                if(!PermissionHandler.readWritePermission(Booking.this)){
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


        addressimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();*/
                Intent intent = new Intent(Booking.this, SingleImage.class);
                intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_USER);
                intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_USER, mImageUri.toString());

                startActivity(intent);
            }
        });




        saveshipmenticontop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyshipmentunitandsave();
            }
        });



        saveshipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyshipmentunitandsave();
            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHandler.onRequestPermissionsResult( requestCode,  permissions, grantResults , Booking.this);

    }

   public  void  verifyshipmentunitandsave(){

               Boolean valid=true;

               valid=true;
               if(bookingdate.getText().toString().equals("")){
                   bookingdate.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                   valid=false;
               }


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
               if(servicetypename.getText().toString().equals("")){
                   servicetypename.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                   valid=false;
               }
               if(shipmenttype.getText().toString().equals("")){
                   shipmenttype.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
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

               if(paymenttypename.getText().toString().equals("")){
                   paymenttypename.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                   valid=false;
               }else {

                   if(paymenttypeid.getText().toString().equals("3")){

                       if(declaredvalue.getText().toString().equals("")){
                           declaredvalue.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                           valid=false;
                       }
                       if(cnee.getText().toString().equals("")){
                           cnee.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                           valid=false;
                       }
                       if(invoicevalue.getText().toString().equals("")){
                           invoicevalue.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                           valid=false;
                       }
                   }
               }

               if(addressimage.getDrawable() == null){
                   Utility.alertDialog(Booking.this,"Alert","Address is Empty ","Ok");
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
            params.put("file",imagetostring(bmp));
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
                    VolleyUtils.furnishErrorMsg(  "Image Save" ,response, Booking.this);
                    return;
                }else {
                   Log.w("responce",""+response.get("id"));
                   SaveShipmentUnit((Integer) response.get("id"));
                }

            }
        });
    }
    private  String imagetostring (Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        Bitmap bmp= VolleyUtils.grabImage(mImageUri,Booking.this);

        bmp= Utility.scaleDown(bmp, 1100, true);


//        Bitmap  bmp1= Bitmap.createScaledBitmap(
//                bmp, 320, 500, false);
        bmp.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes =outputStream.toByteArray();

            return Base64.encodeToString(imageBytes,Base64.DEFAULT);


    }



    public  void  Defaultvalueset(){
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String d= sharedPreferences.getString( ApplicationConstant.DEFAULTVALUE_BOOKING,null);


        Log.w("responce",d);

        DefaultVO defaultVO = gson.fromJson(d, DefaultVO.class);


        servicetypename.setText(defaultVO.getServiceTypeName());
        servicetypeid.setText(defaultVO.getServiceTypeId().toString());
        servicetypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);

        paymenttypename.setText(defaultVO.getPaymentTypeName());
        paymenttypeid.setText(defaultVO.getPaymentTypeId().toString());
        paymenttypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);


        shipmenttype.setText(defaultVO.getShipmentTypeName());
        shipmenttypeid.setText(defaultVO.getShipmentTypeId().toString());
        shipmenttype.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);



    }










    public void SaveShipmentUnit(int id) throws JSONException {

        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            ConnectionVO connectionVO = ShipmentSaveBO.getsaveShipmentUnit();

            sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
            String memberid = (String) sharedPreferences.getString(ApplicationConstant.CACHE_MAMBERID, null);


            CityVO cityVO = new CityVO();
            cityVO.setCityId(Integer.parseInt(cityid.getText().toString()));

            CustomerVO customerVO = new CustomerVO();
            customerVO.setCustomerId(Integer.parseInt(customerid.getText().toString()));

            LastmileVO lastmileVO = new LastmileVO();
            lastmileVO.setLastMileId(Integer.parseInt(forwarderid.getText().toString()));

            ServiceTypeVO serviceTypeVO = new ServiceTypeVO();
            serviceTypeVO.setServiceTypeId(Integer.parseInt(servicetypeid.getText().toString()));
            ShipmentModeVO shipmentModeVO = new ShipmentModeVO();
            shipmentModeVO.setShipmentModeID(Integer.parseInt("1"));

            ShipmentTypeVO shipmentTypeVO = new ShipmentTypeVO();
            shipmentTypeVO.setShipmentTypeId(Integer.parseInt(shipmenttypeid.getText().toString()));

            PaymentTypeVO paymentTypeVO = new PaymentTypeVO();
            paymentTypeVO.setPaymentTypeId(Integer.parseInt(paymenttypeid.getText().toString()));


            ShipmentUnitVO shipmentUnitVO = new ShipmentUnitVO();
            shipmentUnitVO.setAwbNo(bookingawbno.getText().toString());
            shipmentUnitVO.setAnonymousInteger(Integer.parseInt(memberid));
            shipmentUnitVO.setPcs(Integer.parseInt(pcs.getText().toString()));
            shipmentUnitVO.setPincode(pincode.getText().toString());
            shipmentUnitVO.setShipmentWeight(Double.parseDouble(weight.getText().toString()));
            shipmentUnitVO.setDocInvoiceNumber(invoicevalue.getText().toString());
            shipmentUnitVO.setConsigneeName(cnee.getText().toString());
            shipmentUnitVO.setCity(cityVO);
            shipmentUnitVO.setCustomer(customerVO);
            shipmentUnitVO.setLastMile(lastmileVO);
            shipmentUnitVO.setServicetype(serviceTypeVO);
            shipmentUnitVO.setShipmentMode(shipmentModeVO);
            shipmentUnitVO.setShipmentType(shipmentTypeVO);
            shipmentUnitVO.setPaymentType(paymentTypeVO);
            shipmentUnitVO.setFileId(id);
            Date bookingDate = new SimpleDateFormat("dd-MM-yyyy").parse(bookingdate.getText().toString());

            shipmentUnitVO.setBookingDate(bookingDate.getTime());

            if (!declaredvalue.getText().toString().equals("")) {
                shipmentUnitVO.setShipmentValue(Integer.parseInt(declaredvalue.getText().toString()));
            }

            Gson gson = new Gson();
            String json = gson.toJson(shipmentUnitVO);
            JsonObject jsonObject = new JsonObject();

            Log.w("responce", "" + json);

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
                        VolleyUtils.furnishErrorMsg("ShipmentUnit Save", response, Booking.this);
                        return;
                    } else {
                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(Booking.this).create();
                        alertDialog.setTitle("Success");
                        alertDialog.setMessage("Shipment Successfully  Save");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                startActivity(new Intent(Booking.this,Booking.class));
                            }
                        });
                        alertDialog.show();
                    }

                }
            });

        }catch(Exception e){

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
                    VolleyUtils.furnishErrorMsg(  "Pincode" ,response, Booking.this);
                    pincode.setError(Utility.getErrorSpannableString(getApplicationContext(), "Pincode not valid"));
                    datavalid=false;
                }else {
                    pincode.setError(null);
                    datavalid=true;
                    Pincodebycityresponce(response.toString());

                    progressBar.setVisibility(View.GONE);
                }

            }
        });
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
               // bmp = (Bitmap) data.getExtras().get("data");

               // Toast.makeText(this, ""+mImageUri, Toast.LENGTH_SHORT).show();

                try{
                    bmp =VolleyUtils.grabImage(mImageUri,Booking.this);
                    if(bmp.getWidth()>bmp.getHeight()){
                        Matrix matrix =new Matrix();
                        matrix.postRotate(90);
                        bmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
                    }
                    addressimage.setImageBitmap(bmp);
                    addressimage.setVisibility(View.VISIBLE);
                    pincode.setText("");
                    pincode.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);




                 /*   String o=null;
                    ICallback ICallback = new ICallback() {
                        @Override
                        public void callback(String o,EditText pinfiled) {
                            if(o!=null){

                                Log.w("pincode",o);

                                callbackresp(o,pinfiled);

                            };
                        }
                    };*/
                    ImageTextApi b = new ImageTextApi(this, Booking.this, bmp);
                    b.getPincode();

                    pincode.requestFocus();

                   /* if(Utility.getImageByPincode(Booking.this,bmp)!=null){
                        pincode.setText(Utility.getImageByPincode(Booking.this,bmp));
                    };*/

                }catch (Exception e){
                    Toast.makeText(this, "Something Wrong Please Contact ROF Administrator ", Toast.LENGTH_LONG).show();


                    ExceptionsNotification.ExceptionHandling(Booking.this,Utility.getStackTrace(e));
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
                if(Utility.getImageByPincode(Booking.this,bmp)!=null){
                    pincode.setText(Utility.getImageByPincode(Booking.this,bmp));
                };
            }



            if (requestCode == REQ_SERVICETYPE) {
                servicetypename.setText(data.getStringExtra("valueName"));
                servicetypeid.setText(data.getStringExtra("valueId"));

                shipmenttype.requestFocus();
                servicetypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);

            }
            if (requestCode == REQ_SHIPMENTTYPE) {
                shipmenttype.setText(data.getStringExtra("valueName"));
                shipmenttypeid.setText(data.getStringExtra("valueId"));
                shipmenttypeid.setSelection(shipmenttypeid.getText().length());
                shipmenttype.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);

            }
            if (requestCode == REQ_PAYMENTTYPE) {
                paymenttypename.setText(data.getStringExtra("valueName"));
                paymenttypeid.setText(data.getStringExtra("valueId"));


                paymenttypename.setSelection(shipmenttypeid.getText().length());
                paymenttypename.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);

                if(data.getStringExtra("valueId").equals("3")){
                    declardlayout.setVisibility(View.VISIBLE);
                    invoicelayout.setVisibility(View.VISIBLE);
                }else {
                    declardlayout.setVisibility(View.GONE);
                    invoicelayout.setVisibility(View.GONE);
                }

            }



        }
    }


   /* public  void  callbackresp(final String pin,final EditText pinfiled){


        Handler runOnUiThread;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pinfiled.setText(pin);

            }
        });

    }*/


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
        }


    }

    public void customersearchresp(Intent intent){


        customername.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
        customername.setText(intent.getStringExtra("text"));
        customerid.setText(intent.getStringExtra("id"));
        bookingawbno.requestFocus();

        SharedPreferences sharedPreferences= getSharedPreferences("customer", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1= sharedPreferences.edit();
        edit1.putString("customername",customername.getText().toString());
        edit1.putInt("customerid", Integer.valueOf(customerid.getText().toString()));
        edit1.apply();
        edit1.commit();
    }


    protected void onStart()
    {
        super.onStart();
        setcustomer();
    }

    public void setcustomer(){

        SharedPreferences sharedPreferences1= getSharedPreferences("customer", Context.MODE_PRIVATE);
        String name= sharedPreferences1.getString("customername",null);
        int nameid= sharedPreferences1.getInt("customerid",0);

        if(name!=null && nameid!=0 ){
            customername.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
            customername.setText(name);
            customerid.setText(""+nameid);
            customername.setSelection(customername.getText().length());

        }

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
                   forwardername.setText("");
                   forwarderid.setText("");

                   datavalid=false;
               }else {
                   JSONObject response1 = response.getJSONObject("dataList");
                   JSONObject response2 = response1.getJSONObject("lastmile");
                   if(response2.getString("statusId").equals("Active")){
                       bookingawbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, R.drawable.rof_verify_fullcolor ,0);
                       forwardername.setText(response2.getString("name"));
                       forwarderid.setText(response2.getString("lastMileId"));
                       cameraofaddress.performClick();
                       datavalid=true;
                   }else {
                       bookingawbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "This No. already use"));
                       forwardername.setText("");
                       forwarderid.setText("");
                       datavalid=false;
                   }
               }

           }
       });

   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(Booking.this, "Pincode Con't be read ", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}