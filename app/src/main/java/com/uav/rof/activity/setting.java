package com.uav.rof.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.rof.uav.adpater.ImageTextAdapter;
import com.uav.rof.R;
import com.uav.rof.constants.ApplicationConstant;
import com.uav.rof.vo.DataAdapterVO;

import java.util.ArrayList;

public class setting extends AppCompatActivity {
    ImageView back_activity_button;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        back_activity_button=(ImageView)findViewById(R.id.back_activity_button);
        listView=(ListView)findViewById(R.id.listview);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });


        final ArrayList<DataAdapterVO> dataList =  getDataList();
        ImageTextAdapter myAdapter=new ImageTextAdapter(this, dataList, R.layout.listimagewithtextview);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String s=(String)parent.getItemAtPosition(position);
                int i=position;
                DataAdapterVO dataAdapterVO = (DataAdapterVO)dataList.get(position);

                if(dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_SETTING_VERTICAL_KEY_IPSETTING)){
                    startActivity(new Intent(setting.this,IpAddressSetting.class));
                }


                if(dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_SETTING_CAMERA)){
                    startActivity(new Intent(setting.this,Camera_Preview_Setting.class));
                }

                if(dataAdapterVO.getAssociatedValue().equalsIgnoreCase(ApplicationConstant.MENU_SETTING_VERTICAL_KEY_EXIT)){
                    Toast.makeText(setting.this, "Exit", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public  ArrayList<DataAdapterVO> getDataList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();
        DataAdapterVO dataAdapterVO = new DataAdapterVO();
        dataAdapterVO.setText("IP Address Setting");
        dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_SETTING_VERTICAL_KEY_IPSETTING);
        dataAdapterVO.setImage(R.drawable.rof_ipaddress);
        datalist.add(dataAdapterVO);



        dataAdapterVO = new DataAdapterVO();
        dataAdapterVO.setText("App Setting");
        dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_SETTING_CAMERA);
        dataAdapterVO.setImage(R.drawable.rof_camera_btn_icon);
        datalist.add(dataAdapterVO);



        dataAdapterVO = new DataAdapterVO();
        dataAdapterVO.setText("Exit");
        dataAdapterVO.setAssociatedValue(ApplicationConstant.MENU_SETTING_VERTICAL_KEY_EXIT);
        dataAdapterVO.setImage(R.drawable.rof_exit);
        datalist.add(dataAdapterVO);
        return  datalist;
    }
}
