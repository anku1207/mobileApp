package com.rof.uav.adpater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.uav.rof.R;
import com.uav.rof.override.UAVTextView;
import com.uav.rof.vo.DataAdapterVO;

import java.util.ArrayList;

public class TextTextAdapter extends BaseAdapter {
    private Context context;
    ArrayList<DataAdapterVO> dataList;
    private int design;
    private LayoutInflater layoutInflater;
    private int length;

   public TextTextAdapter(Context context, ArrayList<DataAdapterVO> dataList, int design){
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





        UAVTextView textViewL=(UAVTextView) convertView.findViewById(R.id.listtext1);
        UAVTextView textViewR=(UAVTextView) convertView.findViewById(R.id.listtext2);

        DataAdapterVO  dataAdapterVO = (DataAdapterVO)dataList.get(position);

        if(dataAdapterVO.getText().equals("Address")){
            textViewR.setTextColor(Color.parseColor("#12ab55"));
            textViewR.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            textViewR.setTextSize(20);
        }
        textViewL.setText( dataAdapterVO.getText());
        textViewR.setText( dataAdapterVO.getText2());

        return convertView;
    }

}
