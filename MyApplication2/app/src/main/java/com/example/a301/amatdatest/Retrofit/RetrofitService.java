package com.example.a301.amatdatest.Retrofit;

import com.example.a301.amatdatest.Data.CheckVO;
import com.example.a301.amatdatest.Data.LectureVO;
import com.example.a301.amatdatest.Data.NotifyVO;
import com.example.a301.amatdatest.Data.StudentVO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 301 on 2017-09-29.
 */

public interface RetrofitService {
    @GET("/guestclass")
    Call<LectureVO> getLectureData();
    @GET("/gueststudent")
    Call<StudentVO> getStudentData();
    @GET("/guestallCheck")
    Call<CheckVO> getCheckData();
    @GET("/guestnotice")
    Call<NotifyVO> getNotifyData();
    @GET("/guesteNotice")
    Call<NotifyVO> getEnotifyData();
}
