package com.example.a301.amatdatest.Data;

import com.example.a301.amatdatest.BaseActivity;
import com.example.a301.amatdatest.Controller.Constants;
import com.example.a301.amatdatest.Model.Model_Notify;
import com.example.a301.amatdatest.Retrofit.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 301-29 on 2017-10-26.
 */

public class DataManager_Notify {
    public ArrayList<Model_Notify> notifyArrayList;
    public NotifyVO notifyVO;

    public void loadData(){
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);
        Call<NotifyVO> call = service.getNotifyData();
        call.enqueue(new Callback<NotifyVO>() {
            @Override
            public void onResponse(Call<NotifyVO> call, Response<NotifyVO> response) {
                if(response.isSuccessful()){
                    notifyVO=response.body();
                    notifyArrayList=notifyVO.getList();
                    for(int i=0; i< notifyArrayList.size();i++)
                    {
                        String topic =notifyArrayList.get(i).getTopic();
                        String text=notifyArrayList.get(i).getText();
                        String date=notifyArrayList.get(i).getDate();
                        BaseActivity.notifyList.add(new Model_Notify(topic, text, date));
                    }
                }
            }

            @Override
            public void onFailure(Call<NotifyVO> call, Throwable t) {

            }
        });

    }
    public void loadEdata()
    {
        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);
        Call<NotifyVO> call = service.getEnotifyData();
        call.enqueue(new Callback<NotifyVO>() {
            @Override
            public void onResponse(Call<NotifyVO> call, Response<NotifyVO> response) {
                if(response.isSuccessful()){
                    notifyVO=response.body();
                    notifyArrayList=notifyVO.getList();
                    for(int i=0; i< notifyArrayList.size();i++)
                    {
                        String topic =notifyArrayList.get(i).getTopic();
                        String text=notifyArrayList.get(i).getText();
                        String date=notifyArrayList.get(i).getDate();
                        BaseActivity.notifyList.add(new Model_Notify(topic, text, date));
                    }
                }
            }

            @Override
            public void onFailure(Call<NotifyVO> call, Throwable t) {

            }
        });

    }
}
