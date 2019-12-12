package com.uav.rof.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rof.uav.adpater.ImageTextAdapter;
import com.rof.uav.adpater.TextTextAdapter;
import com.uav.rof.R;
import com.uav.rof.bo.AwbQueryBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.util.Utility;
import com.uav.rof.vo.DataAdapterVO;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleySingleton;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AwbQuery extends AppCompatActivity {
    Button awbquerybtn;
    EditText awbno;
    LinearLayout mainlayout;
    ListView listView;

    ArrayList<DataAdapterVO> datalist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awb_query);
        getSupportActionBar().hide();



        listView =(ListView)findViewById(R.id.listview);
        awbquerybtn=(Button)findViewById(R.id.awbquery);
        awbno=(EditText)findViewById(R.id.awbnumber);
        ImageView back_activity_button=(ImageView) findViewById(R.id.back_activity_button);

        mainlayout=(LinearLayout)findViewById(R.id.mainlayout);
        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishactivity();
            }
        });
        awbquerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(awbno.getText().toString().equals("")){
                    awbno.setError(Utility.getErrorSpannableString(getApplicationContext(), "AWB is Empty"));
                    listView.setVisibility(View.GONE);
                    return;
                }else {
                    getawbquerydata();
                }

            }
        });
    }

    public void getawbquerydata(){
        awbno.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0 ,0);
        VolleyUtils.makeJsonObjectRequest(this, AwbQueryBO.getAwbQueryData(awbno.getText().toString()), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;



                if(response.get("status").equals("fail")){
                   // VolleyUtils.furnishErrorMsg(  "Pincode" ,response, AwbQuery.this);
                    awbno.setError(Utility.getErrorSpannableString(getApplicationContext(), response.get("error").toString()));
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    listView.setVisibility(View.GONE);


                }else {
                    awbno.setError(null);
                    datalist.clear();
                   // mainlayout.setVisibility(View.GONE);
                    AwbQueryResponceinServer(response.toString());
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    public void AwbQueryResponceinServer(String s)  {
        JSONObject  jsonObject= null;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        try {
            jsonObject = new JSONObject(s.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("dataList");
            JSONObject operationInformation = jsonObject1.getJSONObject("operationInformation");
            JSONObject jsonObject4 = jsonObject1.getJSONObject("operationInformation");
            final JSONObject booking = jsonObject1.getJSONObject("bookinInformatiion");






            datalist.add(new DataAdapterVO("AWB Status",booking.getString("bookingStatus")));
            datalist.add(new DataAdapterVO("Booking Date",booking.getString("bookingdate")));
            datalist.add(new DataAdapterVO("C Name",booking.getString("customerName")));
            datalist.add(new DataAdapterVO("Forwarder",booking.getString("forwarderName")));
            datalist.add(new DataAdapterVO("Billing Awb",booking.getString("Billingawb")));
            datalist.add(new DataAdapterVO("Billing Forwarder",booking.getString("Billingforworder")));
            datalist.add(new DataAdapterVO("Weight",booking.getString("weight")));
            datalist.add(new DataAdapterVO("Service Type",booking.getString("serviceType")));
            datalist.add(new DataAdapterVO("Payment Type",booking.getString("paymentType")));
            datalist.add(new DataAdapterVO("Destination",booking.getString("Destination")));
            datalist.add(new DataAdapterVO("Shipment Time",booking.getString("DateAndTime")));
            datalist.add(new DataAdapterVO("Address","Click Me"));

            final String[] intArray = {"address","shipmenttype"};




            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // String s=(String)parent.getItemAtPosition(position);

                    DataAdapterVO dataAdapterVO = (DataAdapterVO)datalist.get(position);



                    if(dataAdapterVO.getText().equals("Address")){


                        try {
                            final String url = booking.getString("shipmentAddressPath");
                            Intent intent = new Intent(AwbQuery.this, SingleImage.class);
                            intent.setAction(ApplicationConstant.SINGLE_IMAGE_ACTION_SERVER);
                            intent.putExtra(ApplicationConstant.SINGLE_IMAGE_ACTION_SERVER, url);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                           /* Intent intent = new Intent(AwbQuery.this, SingleImage.class);
                            Log.w("pathfffff",booking.getString("shipmentAddressPath"));
                            intent.putExtra("picture",booking.getString("shipmentAddressPath"));
                            startActivity(intent);*/

                    }
                }
            });




            datalist.add(new DataAdapterVO("Operation Status",jsonObject4.getString("operationStatus")));
            datalist.add(new DataAdapterVO("Rto Receive At",jsonObject4.getString("rtoReceiveAt")));
            datalist.add(new DataAdapterVO("Rto Runsheetno",jsonObject4.getString("rtoRunSheetNo")));
            datalist.add(new DataAdapterVO("Delivered Rto ",jsonObject4.getString("deliveryPersonName")));

            datalist.add(new DataAdapterVO("Receiver ",jsonObject4.getString("receiverName")));
            datalist.add(new DataAdapterVO("Receiver Contact   ",jsonObject4.getString("receiverContact")));



            TextTextAdapter myAdapter=new TextTextAdapter(this, datalist, R.layout.sidebyside_test_listview);

            listView.setAdapter(myAdapter);





        } catch (JSONException e) {
            e.printStackTrace();

        }






    }








    public  void  finishactivity(){
        finish();
    }
}
