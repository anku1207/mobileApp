package com.uav.rof.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.rof.R;
import com.uav.rof.bo.IpAddressBO;
import com.uav.rof.bo.PincodeBO;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.util.Utility;
import com.uav.rof.volley.VolleyResponseListener;
import com.uav.rof.volley.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IpAddressSetting extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText port,ipaddress;
    TextView httptype;
    ImageView back_activity_button;
    Button save;
    SharedPreferences sharedPreferences;
    String protocol,ipAddress,portno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipaddresssetting);
        getSupportActionBar().hide();
        port=(EditText)findViewById(R.id.portno);
        ipaddress=(EditText)findViewById(R.id.ipaddress);
        httptype=(TextView)findViewById(R.id.httptype);
        save=(Button)findViewById(R.id.save);


        getdatasharedpreferences();
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);


        back_activity_button=(ImageView)findViewById(R.id.back_activity_button);
        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sharedPreferences.contains(ApplicationConstant.CACHE_IPVALID)){
                    Toast.makeText(IpAddressSetting.this, "IP Address is empty", Toast.LENGTH_SHORT).show();
                }else {
                    finish();
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        List<String> categories = new ArrayList<String>();
        categories.add("http");
        categories.add("https");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean valid=true;

                valid=true;

                if(port.getText().toString().equals("")){
                    port.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                    valid=false;
                }
                if(ipaddress.getText().toString().equals("")){
                    ipaddress.setError(Utility.getErrorSpannableString(getApplicationContext(), "Empty Fild Not Allow"));
                    valid=false;
                }

                if(valid==true){


                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putString( ApplicationConstant.CACHE_PORT, port.getText().toString());
                    edit.putString( ApplicationConstant.CACHE_IPADDRESS, ipaddress.getText().toString());
                    edit.putString( ApplicationConstant.CACHE_PROTOCOL, httptype.getText().toString());
                    edit.apply();
                    edit.commit();
                    saveipaddress();

                }


            }
        });

    }


    public void saveipaddress() {

        VolleyUtils.makeJsonObjectRequest(this, IpAddressBO.getIpAddtess(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(ApplicationConstant.CACHE_PORT);
                editor.remove( ApplicationConstant.CACHE_IPADDRESS);
                editor.remove(ApplicationConstant.CACHE_PROTOCOL);
                editor.remove(ApplicationConstant.CACHE_IPVALID);
                editor.commit();
            }
            @Override
            public void onResponse(Object resp) throws JSONException {
                JSONObject response = (JSONObject) resp;
                SharedPreferences.Editor edit= sharedPreferences.edit();
                edit.putString( ApplicationConstant.CACHE_IPVALID, "valid");
                edit.apply();
                edit.commit();



                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(IpAddressSetting.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Shipment Successfully  Save");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(IpAddressSetting.this,GmailLogin.class);
                        finish();
                        startActivity(intent);
                    }
                });
                alertDialog.show();







            }
        });
    }






    public  void getdatasharedpreferences(){

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        protocol= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROTOCOL,null);
        ipAddress= (String)sharedPreferences.getString( ApplicationConstant.CACHE_IPADDRESS,null);
        portno= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PORT,null);

        if(protocol!=null && ipAddress != null && portno!=null){
            ipaddress.setText(ipAddress);
            httptype.setText(protocol);
            port.setText(portno);


        }
    }









    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        httptype.setText(item);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //moveTaskToBack(false);
        if(!sharedPreferences.contains(ApplicationConstant.CACHE_IPVALID)){
            Toast.makeText(IpAddressSetting.this, "IP Address is empty", Toast.LENGTH_SHORT).show();
        }else {
            finish();
        }
    }
}
