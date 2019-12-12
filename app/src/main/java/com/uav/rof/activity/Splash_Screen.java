package com.uav.rof.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.uav.rof.R;
import com.uav.rof.constants.ApplicationConstant;

public class Splash_Screen extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);


        ImageView imageView = (ImageView) findViewById( R.id.appstarticon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dowork();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                staarapp();
                finish();

            }
        }).start();
    }

    private  void dowork() throws InterruptedException {
        for(int i=0; i<100;i+=10){
            Thread.sleep(100);
            progressBar.setProgress(i);

        }
    }

    private  void staarapp(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
       /* String protocol= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PROTOCOL,null);
        String ipAddress= (String)sharedPreferences.getString( ApplicationConstant.CACHE_IPADDRESS,null);
        String portno= (String)sharedPreferences.getString( ApplicationConstant.CACHE_PORT,null);

        Boolean jj=  sharedPreferences.contains(ApplicationConstant.CACHE_IPVALID);*/



         if(!sharedPreferences.contains(ApplicationConstant.CACHE_IPVALID) ){
            Intent intent=new Intent(this,IpAddressSetting.class);
            finish();
            startActivity(intent);
        }else {
            Intent intent=new Intent(this,GmailLogin.class);
            finish();
            startActivity(intent);
        }


       // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

