package com.example.a301.amatdatest.Data;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.a301.amatdatest.BaseActivity;
import com.example.a301.amatdatest.Controller.Constants;
import com.example.a301.amatdatest.LoginActivity;
import com.example.a301.amatdatest.Model.Model_Lecture;
import com.example.a301.amatdatest.Model.Model_Student;
import com.example.a301.amatdatest.Retrofit.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 301 on 2017-09-30.
 */

public class DataManager_Student {

    public ArrayList<Model_Student> tempList2;
    public StudentVO repoList2;

    public LectureVO repoList;
    public ArrayList<Model_Lecture> tempList;

    public void loadData(final Activity activity)
    {
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        Call<StudentVO> call = service.getStudentData();
        call.enqueue(new Callback<StudentVO>() {
            @Override
            public void onResponse(Call<StudentVO> call, Response<StudentVO> response) {
                if (response.isSuccessful()) {
                    repoList2 = response.body();
                    tempList2 = repoList2.getList();

                    for (int i = 0; i < tempList2.size(); i++) {
                        String studentNum = tempList2.get(i).getStudentNum();
                        String lecture1 = tempList2.get(i).getLecture1();
                        String lecture2 = tempList2.get(i).getLecture2();
                        String lecture3 = tempList2.get(i).getLecture3();
                        String password= tempList2.get(i).getPassword();
                        String name= tempList2.get(i).getName();
                        String foreigner =tempList2.get(i).getForeiner();

                        BaseActivity.studentList.add(new Model_Student(studentNum, lecture1, lecture2, lecture3,password,name,foreigner));
                    }

                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();


                }
            }

            @Override
            public void onFailure(Call<StudentVO> call, Throwable t) {

                Toast.makeText(activity, "서버와 연결할수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
