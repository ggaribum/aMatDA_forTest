package com.example.a301.amatdatest;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

/**
 * Created by 301 on 2017-09-30.
 */

public class BaseApplication extends Application {
    private BeaconManager beaconManager;
    public int soundSet=-1;

    public Region region1;
    public Region region2;

    public void onCreate(){
        super.onCreate();
        beaconManager=new BeaconManager(getApplicationContext());
        Log.d("TestING","베이스애플리케이션 실행됨");
        region1=new Region("monitored region",
                UUID.fromString("11111111-1111-1111-1111-111111111404"),4660,64001
        );
        region2=new Region("monitored region",
                UUID.fromString("11111111-1111-1111-1111-111111111301"),4660,64001
        );

        //설치 후 beaconMonitoring service 시작
        //app 종료 하더라도 service
        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady() {
                /*beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString("11111111-1111-1111-1111-111111111301"),4660,64001
                ));*/
                beaconManager.startMonitoring(region1);
                beaconManager.startMonitoring(region2);


            }
        });

        //비콘감지
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener(){

            Context context = getApplicationContext();
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                // 110 120 비콘이 잡히면
                Log.v("TestING","엔터");
                soundSet=mAudioManager.getRingerMode();
                if(!list.isEmpty()) {
                    Log.v("TestING","리스트");
                    Beacon beacon = list.get(0);
                    if(beacon.getRssi()>-58){ // 93-0
                        if(!isAlreadyRunActivity())
                        {
                            /*//앱 실행중이 아닐때
                            Intent intent =new Intent(getApplicationContext(),NoBackgroundActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("excuteType","beacon");
                            getApplicationContext().startActivity(intent);
                            showNotification("M4M", "매너 있게 변경되었습니다");*/
                        }
                        else {
                            showNotification("아맞다","C 프로그래밍 및 실습 출석 완료. 매너있게 변경되었습니다.");
                        }
                    }
                    else{ //94-110
                    // showNotification("M4M", "매너 공간이 아닙니다");
                    }

                }
                else {
                    //list에 없다가 잡혔을 경우에는 ?
                  //  showNotification("M4M", "매너 있게 바뀌기 직전 입니다.");
                }

            }
            @Override
            public void onExitedRegion(Region region) {


            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String message){
        Intent notifyIntent = new Intent(this, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(
                this, 0, new Intent[]{notifyIntent}
                , PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification=new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.make)
                .setTicker("[M4M] 모드가 변경되었습니다")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }
    private boolean isAlreadyRunActivity()
    {
        ActivityManager activity_manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);
        String activityName = "";
        for( int i = 0; i < task_info.size(); i++ ) {
            activityName = task_info.get(i).topActivity.getPackageName();
            if( activityName.startsWith("com.example.a301.myapplication") ) {
                return true; }
        }

        return false;
    }
}
