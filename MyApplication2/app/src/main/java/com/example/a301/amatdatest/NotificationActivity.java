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

import com.example.a301.amatdatest.Controller.Adapter_Notify;
import com.example.a301.amatdatest.Controller.BottomNavigationViewHelper;
import com.example.a301.amatdatest.Controller.TimeManager;
import com.example.a301.amatdatest.Model.Model_Notify;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

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
                    Intent intent2 = new Intent(getApplicationContext(),ScheduleActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notification:
                    return true;
            }
            return false;
        }

    };
    private RecyclerView recyclerView;
    ArrayList<Model_Notify> notifyArrayList;
    Adapter_Notify adapter_notify;
    LinearLayoutManager manager;

    TextView tv_data;
    TextView tv_date;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=(RecyclerView) findViewById(R.id.rv_notify);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        tv_data = (TextView) findViewById(R.id.tv_data_noti);
        tv_date = (TextView) findViewById(R.id.tv_date_noti);

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
                Toast.makeText(NotificationActivity.this, BaseActivity.currentStudent+ "님이 로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();


            }
        });

        tv_data.setText("["+ BaseActivity.currentName+"]" + " (" + BaseActivity.currentStudent + ") ");
        if(BaseActivity.foreignerFlag)
        {
            tv_date.setText(new TimeManager().getEcurrentDate());
        }
        else {tv_date.setText(new TimeManager().getCurrentDate());}

        adapter_notify= new Adapter_Notify(BaseActivity.notifyList,getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter_notify);
        recyclerView.smoothScrollToPosition(0);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_notification);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigation.setSelectedItemId(R.id.navigation_notification);

    }

}
