package com.example.a301.amatdatest.Data;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.a301.amatdatest.BaseActivity;
import com.example.a301.amatdatest.Controller.Constants;
import com.example.a301.amatdatest.MainActivity;
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

public class DataManager_Lecture {

    public ArrayList<Model_Student> tempList2;
    public StudentVO repoList2;

    public LectureVO repoList;
    public ArrayList<Model_Lecture> tempList;

    public void loadData(final Activity activity)
    {
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        Call<LectureVO> call2 = service.getLectureData();
        call2.enqueue(new Callback<LectureVO>() {
            @Override
            public void onResponse(Call<LectureVO> call, Response<LectureVO> response) {
                if (response.isSuccessful()) {
                    repoList = response.body();
                    tempList = repoList.getList();

                    for (int i = 0; i < tempList.size(); i++) {
                        String lecture = tempList.get(i).getLecture();
                        String lectureRoom = tempList.get(i).getLectureRoom();
                        String lectureStartTime = tempList.get(i).getLectureStartTime();
                        String lectureFinishTime = tempList.get(i).getLectureFinishTime();
                        String lectureDay = tempList.get(i).getLectureDay();
                        String professor = tempList.get(i).getProfessor();
                        BaseActivity.lectureList.add(new Model_Lecture(lecture, lectureRoom, lectureStartTime, lectureFinishTime, lectureDay,professor));
                    }

                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                }
            }

            @Override
            public void onFailure(Call<LectureVO> call, Throwable t) {
                Log.v("fuck","fucking실패");
            }
        });

    }
}
