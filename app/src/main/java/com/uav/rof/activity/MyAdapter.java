package com.uav.rof.activity;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uav.rof.R;
import com.uav.rof.override.UAVTextView;
import com.uav.rof.vo.DataAdapterVO;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anku on 8/9/2017.
 */
/*
public class MyAdapter extends BaseAdapter {
    private Context context;
    ArrayList<DataAdapterVO> dataList;
    private int design;
    private LayoutInflater layoutInflater;
    private int length;

    MyAdapter(Context context, ArrayList<DataAdapterVO> dataList, int design){
        this.context=context;
        this.dataList = dataList;
        this.design = design;
        layoutInflater=((Activity)context).getLayoutInflater();
        this.length = dataList.size();
    }
    @Override
    public int getCount() {
        return this.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = LayoutInflater.from(context).
                inflate(this.design, parent, false);




        ImageView imageView=(ImageView)convertView.findViewById(R.id.listimage);
        UAVTextView textView=(UAVTextView) convertView.findViewById(R.id.listtext);

        DataAdapterVO  dataAdapterVO = (DataAdapterVO)dataList.get(position);
        textView.setTxtAssociatedValue( dataAdapterVO.getAssociatedValue());
        imageView.setImageResource(  dataAdapterVO.getImage());
        textView.setText( dataAdapterVO.getText());



        return convertView;
    }
}
*/