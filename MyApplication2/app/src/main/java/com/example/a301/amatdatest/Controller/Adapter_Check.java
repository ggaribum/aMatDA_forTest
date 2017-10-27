package com.example.a301.amatdatest.Controller;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a301.amatdatest.BaseActivity;
import com.example.a301.amatdatest.Model.Model_Check;
import com.example.a301.amatdatest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 301 on 2017-10-01.
 */

public class Adapter_Check extends  RecyclerView.Adapter<Adapter_Check.ViewHolder>{


    List<Model_Check> tempArr;
    Context adapterContext;


    public Adapter_Check(Context context, ArrayList<Model_Check> tempArr) {
        this.adapterContext = context;
        this.tempArr = tempArr;
    }

    @Override
    public Adapter_Check.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check, parent, false));
    }

    @Override
    public void onBindViewHolder(Adapter_Check.ViewHolder holder, int position) {


        String date = tempArr.get(position).getAttendanceDay();
        String dateTemp1 = date.substring(0, 2);
        String dateTemp2 = date.substring(2, 4);
        date = dateTemp1 + "/" + dateTemp2;
        holder.lectureRoomTv.setText(date);

        String time = tempArr.get(position).getAttendanceTime();
        if(!time.equals("추석")) {
            if (!time.isEmpty()) {
                String timeTemp1 = time.substring(0, 2);
                String timeTemp2 = time.substring(2, 4);
                time = timeTemp1 + ":" + timeTemp2;
            } else {
                if(BaseActivity.foreignerFlag)
                {
                    time="None";
                }
                else {
                    time = "없음";
                }
            }
        }
        holder.lectureTimeTv.setText(time);
        holder.lectureTv.setText(tempArr.get(position).getLecture());
        switch (tempArr.get(position).getAttendanceState()) {
            case "출석":
                holder.lectureDateTv.setTextColor(Color.parseColor("#0099cc"));
                break;
            case "지각":
                holder.lectureDateTv.setTextColor(Color.parseColor("#e8b12b"));
                break;
            case "결석":
                holder.lectureDateTv.setTextColor(Color.RED);
                break;
            case "○":
                holder.lectureDateTv.setTextColor(Color.parseColor("#0099cc"));
                break;
            case "△":
                holder.lectureDateTv.setTextColor(Color.parseColor("#e8b12b"));
                break;
            case "X":
                holder.lectureDateTv.setTextColor(Color.RED);
                break;
        }
        holder.lectureDateTv.setText(tempArr.get(position).getAttendanceState());
    }


    @Override
    public int getItemCount() {
        return tempArr.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView lectureTv;
        public TextView lectureRoomTv;
        public TextView lectureTimeTv;
        public TextView lectureDateTv;



        public ViewHolder(View itemView){
            super(itemView);
            Log.v("AdapterTest","viewHolder클래스");

            lectureTv= (TextView) itemView.findViewById(R.id.lecture_TextView);
            lectureRoomTv=(TextView)itemView.findViewById(R.id.lectureRoom_TextView);
            lectureDateTv=(TextView)itemView.findViewById(R.id.lectureDate_TextView);
            lectureTimeTv=(TextView)itemView.findViewById(R.id.lectureTime_TextView);

        }

    }
}
