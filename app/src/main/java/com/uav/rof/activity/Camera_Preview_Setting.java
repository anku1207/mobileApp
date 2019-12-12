package com.uav.rof.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.uav.rof.R;
import com.uav.rof.constants.ApplicationConstant;

public class Camera_Preview_Setting extends AppCompatActivity {
    Switch on_off_switch;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera__preview__setting);
        ImageView back_activity_button=(ImageView)findViewById(R.id.back_activity_button);

        getSupportActionBar().hide();
        on_off_switch=(Switch)findViewById(R.id.on_off_switch);

        sharedPreferences = getSharedPreferences(ApplicationConstant.SHAREDPREFENCE,  Context.MODE_PRIVATE);
        String data= (String)sharedPreferences.getString(ApplicationConstant.CACHE_CAMERA_PREVIEW ,null);


        if(data==null || data.equals("true")){
            on_off_switch.setChecked(true);
        }else{
            on_off_switch.setChecked(false);
        }

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        on_off_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit= sharedPreferences.edit();

                if (isChecked) {
                    edit.putString( ApplicationConstant.CACHE_CAMERA_PREVIEW,"true");
                } else {
                    edit.putString( ApplicationConstant.CACHE_CAMERA_PREVIEW,"false");
                }
                edit.apply();
                edit.commit();

            }

        });



    }
}
