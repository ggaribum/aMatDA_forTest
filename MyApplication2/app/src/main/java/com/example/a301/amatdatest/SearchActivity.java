package com.example.a301.amatdatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.a301.amatdatest.Controller.Adapter_Check;
import com.example.a301.amatdatest.Controller.Constants;
import com.example.a301.amatdatest.Controller.TimeManager;
import com.example.a301.amatdatest.Data.CheckVO;
import com.example.a301.amatdatest.Model.Model_Check;
import com.example.a301.amatdatest.Retrofit.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    public ArrayList<Model_Check> tempList;
    public ArrayList<Model_Check> showList;
    public CheckVO repoList;
    Intent getIntent;
    String lecture;
    String studentNm;

    RecyclerView checkRv;

    TextView tv_data;
    TextView tv_date;

    LinearLayoutManager manager;
    Adapter_Check adapter_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getIntent= new Intent(this.getIntent());
        lecture=getIntent.getStringExtra("lecture");
        studentNm=BaseActivity.currentStudent;

        tempList=new ArrayList<>();
        showList=new ArrayList<>(); //위치 바꿔야할듯

        checkRv =(RecyclerView)findViewById(R.id.checkRecycler);

        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        tv_data = (TextView) findViewById(R.id.tv_data_sear);
        tv_date = (TextView) findViewById(R.id.tv_date_sear);

        tv_data.setText("["+ BaseActivity.currentName+"]" + " (" + BaseActivity.currentStudent + ") ");
        if(BaseActivity.foreignerFlag)
        {
            tv_date.setText(new TimeManager().getEcurrentDate());
        }
        else {tv_date.setText(new TimeManager().getCurrentDate());}


        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        Call<CheckVO> call = service.getCheckData();
        call.enqueue(new Callback<CheckVO>() {
            @Override
            public void onResponse(Call<CheckVO> call, Response<CheckVO> response) {
               if(response.isSuccessful()) {
                   repoList = response.body();
                   tempList=repoList.getList();

                   for(int i=0; i< tempList.size();i++)
                   {
                       if(tempList.get(i).getStudentNum().equals(studentNm) && tempList.get(i).getLecture().equals(lecture))
                       {

                           //오늘 MMdd 구해서 비교
                           long now = System.currentTimeMillis();
                           Date nowDate = new Date(now);
                           SimpleDateFormat sdfNow = new SimpleDateFormat("MMdd");
                           Calendar cal = Calendar.getInstance();
                           cal.setTime(nowDate);
                           String nowTime = sdfNow.format(cal.getTime());

                           int nowMMdd=Integer.parseInt(nowTime);
                           int checkMMdd=Integer.parseInt(tempList.get(i).getAttendanceDay());
                           Log.d("레이아웃수정중","now : "+nowMMdd);
                           Log.d("레이아웃수정중","수업 : "+checkMMdd);

                           if(nowMMdd>=checkMMdd) {
                               String studentNum = tempList.get(i).getStudentNum();
                               String attendanceDay = tempList.get(i).getAttendanceDay();
                               String attendanceTime = tempList.get(i).getAttendanceTime();
                               String attendanceState = tempList.get(i).getAttendanceState();
                               String lecture = tempList.get(i).getLecture();

                               showList.add(new Model_Check(studentNum, attendanceDay, attendanceTime, attendanceState, lecture));
                           }
                       }
                   }

                   adapter_check = new Adapter_Check(getApplicationContext(),showList);
                   checkRv.setLayoutManager(manager);
                   checkRv.setAdapter(adapter_check);

               }



            }

            @Override
            public void onFailure(Call<CheckVO> call, Throwable t) {

            }
        });


    }
}
