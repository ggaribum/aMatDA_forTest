package com.example.a301.amatdatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.a301.amatdatest.Data.DataManager_Lecture;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        DataManager_Lecture dmL = new DataManager_Lecture();
        dmL.loadData(this);

    }
}
