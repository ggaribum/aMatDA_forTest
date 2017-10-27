package com.example.a301.amatdatest.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a301.amatdatest.Model.Model_Lecture;
import com.example.a301.amatdatest.R;
import com.example.a301.amatdatest.SearchActivity;

import java.util.List;

/**
 * Created by 301 on 2017-09-29.
 */

public class Adapter_Lecture extends RecyclerView.Adapter<Adapter_Lecture.ViewHolder>{

    List<Model_Lecture> tempArr;
    Context adapterContext;
    Intent intent;

    public Adapter_Lecture(Context context, List<Model_Lecture> tempArr) {
        this.adapterContext = context;
        this.tempArr = tempArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        intent = new Intent(adapterContext, SearchActivity.class);

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false));
    }

    @Override
    public void onBindViewHolder(final Adapter_Lecture.ViewHolder holder, int position) {

        holder.lectureTv.setText(tempArr.get(position).getLecture());
        holder.lectureRoomTv.setText(tempArr.get(position).getLectureRoom());

        String start1=tempArr.get(position).getLectureStartTime().substring(0,2);
        String start2=tempArr.get(position).getLectureStartTime().substring(2,4);
        String finish1=tempArr.get(position).getLectureFinishTime().substring(0,2);
        String finish2=tempArr.get(position).getLectureFinishTime().substring(2,4);
        holder.lectureTimeTv.setText(start1+":"+start2+"~"+finish1+":"+finish2);

        holder.lectureDateTv.setText(tempArr.get(position).getLectureDay());
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //put
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("lecture",holder.lectureTv.getText().toString());
                adapterContext.startActivity(intent);
            }
        });
        holder.professorTv.setText(tempArr.get(position).getProfessor());

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
        public TextView professorTv;
        public LinearLayout test;


        public ViewHolder(View itemView){
            super(itemView);
            Log.v("AdapterTest","viewHolder클래스");

            test=(LinearLayout)itemView.findViewById(R.id.testItem);
            lectureTv= (TextView) itemView.findViewById(R.id.lecture_TextView);
            lectureRoomTv=(TextView)itemView.findViewById(R.id.lectureRoom_TextView);
            lectureDateTv=(TextView)itemView.findViewById(R.id.lectureDate_TextView);
            lectureTimeTv=(TextView)itemView.findViewById(R.id.lectureTime_TextView);
            professorTv=(TextView)itemView.findViewById(R.id.lecturePro_TextView);


        }

    }
}
