package com.uav.rof.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.uav.exceptions.ExceptionsNotification;
import com.uav.rof.R;
import com.uav.rof.bo.AwbnoBO;
import com.uav.rof.bo.PickupConfirmationBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.util.Utility;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PickupConfirmation extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<String> entityText;
    ArrayList<Integer> entityId;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picup_confirmation);
        getSupportActionBar().hide();

        final ListView listView =findViewById(R.id.listview);


        entityText = new ArrayList<String>();
        entityId = new ArrayList<Integer>();

        ImageView back_activity_button=findViewById(R.id.back_activity_button);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE, Context.MODE_PRIVATE);
        String memberid = (String) sharedPreferences.getString(ApplicationConstant.CACHE_MAMBERID, null);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PickupConfirmation.this, listView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                sendMail(entityId.get(i).toString());
            }
        });




        VolleyUtils.makeJsonObjectRequest(this, PickupConfirmationBO.getConfirmationVendor(memberid), new VolleyResponseListener() {
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
                    Utility.alertDialog(PickupConfirmation.this,"Alert",sb.toString(),"Ok");
                }else {

                    JSONArray jsonArray = response.getJSONArray("dataList");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        entityText.add(jsonObject1.getString("name" ));
                        entityId.add(jsonObject1.getInt("lastMileId" ));
                    }

                    adapter = new ArrayAdapter(PickupConfirmation.this, R.layout.activity_listtextview, R.id.textdata, entityText);
                    listView.setAdapter(adapter);
                }
            }
        });
    }



    public void sendMail(String attorneyId){

        VolleyUtils.makeJsonObjectRequest(this, PickupConfirmationBO.sendMailToVendor(attorneyId), new VolleyResponseListener() {
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
                    Utility.alertDialog(PickupConfirmation.this,"Alert",sb.toString(),"Ok");
                }else {
                    Utility.alertDialog(PickupConfirmation.this,"Response",response.getString("message"),"Ok");


                }
            }
        });

    }
}
