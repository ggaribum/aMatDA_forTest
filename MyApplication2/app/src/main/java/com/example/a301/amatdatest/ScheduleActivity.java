package com.example.a301.amatdatest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a301.amatdatest.Controller.Adapter_Lecture;
import com.example.a301.amatdatest.Controller.BottomNavigationViewHelper;
import com.example.a301.amatdatest.Controller.TimeManager;
import com.example.a301.amatdatest.Model.Model_Lecture;
import com.example.a301.amatdatest.Model.Model_Student;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    TextView tv_data;
    TextView tv_date;

    RecyclerView scheduleRv;

    LinearLayoutManager manager;
    Adapter_Lecture adapter_schedule;
    ArrayList<Model_Lecture> tempList;
    ArrayList<Model_Student> currentSTUlist;
    private BottomNavigationView navigation;
    ImageView imageView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_schedule:
                    return true;
                case R.id.navigation_notification:
                    Intent intent2 = new Intent(getApplicationContext(),NotificationActivity.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        imageView=(ImageView)findViewById(R.id.btn_logout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BaseActivity.class);

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(ScheduleActivity.this, BaseActivity.currentStudent+ "님이 로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();


            }
        });

        tempList=new ArrayList<>();
        currentSTUlist= new ArrayList<>();
        for(int i=0; i<BaseActivity.studentList.size();i++) {
            if(BaseActivity.studentList.get(i).getStudentNum().equals(BaseActivity.currentStudent))
            {
                String _studentNum = BaseActivity.studentList.get(i).getStudentNum();
                String _lecture1 = BaseActivity.studentList.get(i).getLecture1();
                String _lecture2 = BaseActivity.studentList.get(i).getLecture2();
                String _lecture3 = BaseActivity.studentList.get(i).getLecture3();
                String _password= BaseActivity.studentList.get(i).getPassword();
                String _name =BaseActivity.studentList.get(i).getName();
                String _foreigner=BaseActivity.studentList.get(i).getForeiner();
                currentSTUlist.add(new Model_Student(_studentNum, _lecture1, _lecture2, _lecture3,_password,_name,_foreigner));
            }
        }

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_schedule);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        scheduleRv=(RecyclerView)findViewById(R.id.scheduleRecycler);

        tv_data = (TextView) findViewById(R.id.tv_data_sche);
        tv_date = (TextView) findViewById(R.id.tv_date_sche);

        tv_data.setText("["+ currentSTUlist.get(0).getName()+"]" + " (" + BaseActivity.currentStudent + ") ");
        if(BaseActivity.foreignerFlag)
        {
            tv_date.setText(new TimeManager().getEcurrentDate());
        }
        else {tv_date.setText(new TimeManager().getCurrentDate());}

        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        for (int i = 0; i < BaseActivity.lectureList.size(); i++) {

            if (currentSTUlist.get(0).getLecture1().equals(BaseActivity.lectureList.get(i).getLecture())
                    || currentSTUlist.get(0).getLecture2().equals(BaseActivity.lectureList.get(i).getLecture())
                    || currentSTUlist.get(0).getLecture3().equals(BaseActivity.lectureList.get(i).getLecture())
                    )
            {

                    String lecture = BaseActivity.lectureList.get(i).getLecture();
                    String lectureRoom = BaseActivity.lectureList.get(i).getLectureRoom();
                    String lectureStartTime = BaseActivity.lectureList.get(i).getLectureStartTime();
                    String lectureFinishTime = BaseActivity.lectureList.get(i).getLectureFinishTime();
                    String lectureDay = BaseActivity.lectureList.get(i).getLectureDay();
                    String professor =BaseActivity.lectureList.get(i).getProfessor();
                    tempList.add(new Model_Lecture(lecture, lectureRoom, lectureStartTime, lectureFinishTime, lectureDay,professor));

            }
        }

        adapter_schedule = new Adapter_Lecture(getApplicationContext(),tempList);
        scheduleRv.setLayoutManager(manager);
        scheduleRv.setAdapter(adapter_schedule);



    }


    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.navigation_schedule);

    }

}
