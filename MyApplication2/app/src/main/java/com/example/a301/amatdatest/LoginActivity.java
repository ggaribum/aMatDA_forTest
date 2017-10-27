package com.example.a301.amatdatest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a301.amatdatest.Data.DataManager_Notify;

public class LoginActivity extends Activity {

    EditText edt_id;
    EditText edt_pw;
    Button btn_login;
    String loginId, loginPwd;

    //String studentNum;
    //String password;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(n.isNotificationPolicyAccessGranted()) {
            AudioManager mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }else{
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }

        edt_id = (EditText) findViewById(R.id.edt_id);
        edt_pw = (EditText) findViewById(R.id.edt_pw);
        btn_login = (Button) findViewById(R.id.btn_login);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("edt_id",null);
        loginPwd = auto.getString("edt_pw",null);


        if(loginId!=null ){
            Log.d("뭔데 시발아888888",edt_id.getText().toString());
            for(int i=0;i<BaseActivity.studentList.size();i++)
            {
                if(loginId.equals(BaseActivity.studentList.get(i).getStudentNum()))
                {
                    if(BaseActivity.studentList.get(i).getForeiner().equals("1"))
                    {
                        BaseActivity.foreignerFlag=true;
                        DataManager_Notify dmN = new DataManager_Notify();
                        dmN.loadEdata();
                    }
                    else
                    {
                        BaseActivity.foreignerFlag=false;
                        DataManager_Notify dmN = new DataManager_Notify();
                        dmN.loadData();
                    }
                    BaseActivity.currentStudent =loginId;
                    BaseActivity.currentName=BaseActivity.studentList.get(i).getName();
                    Toast.makeText(LoginActivity.this, BaseActivity.currentStudent +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        }
        else if(loginId ==null ) {

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d("뭔데 시발아7777777",edt_id.getText().toString());
                    boolean loginFlag = false;

                    for (int i = 0; i < BaseActivity.studentList.size(); i++) {
                        if (edt_id.getText().toString().equals(BaseActivity.studentList.get(i).getStudentNum())
                                && edt_pw.getText().toString().equals(BaseActivity.studentList.get(i).getPassword())) { // 여기에 && 사용, 비밀번호 일치 여부도 구현
                            Log.d("뭔데 시발아2222",edt_id.getText().toString());

                            BaseActivity.currentStudent =edt_id.getText().toString();
                            BaseActivity.currentName=BaseActivity.studentList.get(i).getName();
                            if(BaseActivity.studentList.get(i).getForeiner().equals("1"))
                            {
                                BaseActivity.foreignerFlag=true;
                                DataManager_Notify dmN = new DataManager_Notify();
                                dmN.loadEdata();
                            }
                            else
                            {
                                BaseActivity.foreignerFlag=false;
                                DataManager_Notify dmN = new DataManager_Notify();
                                dmN.loadData();
                            }

                            Log.d("뭔데 시발아",edt_id.getText().toString());

                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = auto.edit();

                            autoLogin.putString("edt_id", edt_id.getText().toString());
                            autoLogin.putString("edt_pw", edt_pw.getText().toString());
                            autoLogin.commit();

                            Log.d("뭔데 시발아11111",edt_id.getText().toString());


                            Toast.makeText(LoginActivity.this, edt_id.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();

                            loginFlag = true;
                            Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                    if (!loginFlag)
                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "뭥미?.", Toast.LENGTH_SHORT).show();

        }
    }
}
