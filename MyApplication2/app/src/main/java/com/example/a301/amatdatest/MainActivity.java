package com.example.a301.amatdatest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.a301.amatdatest.Controller.Adapter_Lecture;
import com.example.a301.amatdatest.Controller.BottomNavigationViewHelper;
import com.example.a301.amatdatest.Controller.Constants;
import com.example.a301.amatdatest.Controller.TimeManager;
import com.example.a301.amatdatest.Data.CheckVO;
import com.example.a301.amatdatest.Model.Model_Check;
import com.example.a301.amatdatest.Model.Model_Lecture;
import com.example.a301.amatdatest.Model.Model_Student;
import com.example.a301.amatdatest.Retrofit.RetrofitService;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    RecyclerView rv_lecuture;
    LinearLayoutManager manager;
    Adapter_Lecture adapter_lecture;
    public static ArrayList<Model_Lecture> adapterList;
    public int soundSet=-1;

    public String strFinishTime="2359";

    private BeaconManager beaconManager;
    private Region region;
    private Region region2;

    public ArrayList<Model_Lecture> tempList;
    public ArrayList<Model_Lecture> todayList;

    public ArrayList<Model_Check> checkList;
    public ArrayList<Model_Check> checkTempList;
    public CheckVO repo;

    public ArrayList<Model_Student> currentSTUlist;

    TextView tv_data;
    TextView tv_date;
    ImageView btn_logout;


    private boolean FLAG = false;
    private boolean silentFLAG = false;

    //public String finishThisTime = "2359";


    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_schedule:
                    Intent intent1 = new Intent(getApplicationContext(),ScheduleActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_notification:
                    Intent intent2 = new Intent(getApplicationContext(),NotificationActivity.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("여기는 메인"," ");

        btn_logout=(ImageView)findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BaseActivity.class);

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });


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

        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_date = (TextView) findViewById(R.id.tv_date);

        tv_data.setText("["+ currentSTUlist.get(0).getName()+"]" + " (" + BaseActivity.currentStudent + ") ");
        if(BaseActivity.foreignerFlag)
        {
            tv_date.setText(new TimeManager().getEcurrentDate());
        }
        else {tv_date.setText(new TimeManager().getCurrentDate());}

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /////////////////////////////비콘엑티비티////////////////////////////////////////////
        adapterList = new ArrayList<>();

        for (int i = 0; i < BaseActivity.lectureList.size(); i++) {
            if (currentSTUlist.get(0).getLecture1().equals(BaseActivity.lectureList.get(i).getLecture())
                    || currentSTUlist.get(0).getLecture2().equals(BaseActivity.lectureList.get(i).getLecture())
                    || currentSTUlist.get(0).getLecture3().equals(BaseActivity.lectureList.get(i).getLecture())
                    ) {
                //현재요일 구하기
                String today;
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                Calendar cal = Calendar.getInstance();
                today = Constants.switchDAY(cal.get(Calendar.DAY_OF_WEEK));
                //Log.v("TodayIs", today);
                if (BaseActivity.lectureList.get(i).getLectureDay().contains(today)) {
                    String lecture = BaseActivity.lectureList.get(i).getLecture();
                    String lectureRoom = BaseActivity.lectureList.get(i).getLectureRoom();
                    String lectureStartTime = BaseActivity.lectureList.get(i).getLectureStartTime();
                    String lectureFinishTime = BaseActivity.lectureList.get(i).getLectureFinishTime();
                    String lectureDay = BaseActivity.lectureList.get(i).getLectureDay();
                    String professor =BaseActivity.lectureList.get(i).getProfessor();
                    adapterList.add(new Model_Lecture(lecture, lectureRoom, lectureStartTime, lectureFinishTime, lectureDay,professor));
                }
            }
        }





        //오늘 강의 리스트에 저장.
        todayList = new ArrayList<>();
        todayList = MainActivity.adapterList;


        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {


            Context context = getApplicationContext();
            AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


            //여기가 무한반복
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                tempList = new ArrayList<Model_Lecture>();
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    Log.d("Beacon", "Nearest place: " + nearestBeacon.getRssi());


                    //301 기준 수신강도가 95보다 크면 실내 무음모드 전환
                    if (nearestBeacon.getRssi() > -93) {

                        Log.d("THISFLAG", "IS:  " + FLAG);

                        //현재시간을 구해서 nowIntTime 변수에 담기
                        String tempStr[] = switchNowTime().split(" ");
                        String nowDate = tempStr[0];
                        String nowTime = tempStr[1];
                        int nowIntTime = Integer.parseInt(nowTime);
                        int intFinishTime =Integer.parseInt(strFinishTime);

                            if (intFinishTime <= nowIntTime) {
                                FLAG = false;
                                silentFLAG = false;
                                if (soundSet == 0) {
                                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                } else if (soundSet == 1) {
                                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                } else if (soundSet == 2) {
                                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                }
                            }

                        // 지금수업중이던 수업이 끝나고 나면 다시 수업 탐색, 원래 소리로 복귀


                        //출결처리 된 상태 && 비콘구역이라면
                        if (silentFLAG) {
                            setSilent(mAudioManager);
                        }


                        //수업 출석비교부분
                        if (!FLAG) {
                            Log.d("THISFLAG", "in the IF IS:  " + FLAG);

                            String roomNum = nearestBeacon.getProximityUUID().toString();
                            int last = roomNum.length();
                            roomNum = roomNum.substring(last - 3, last);

                            Log.d("THIS roomNum", "in the IF IS  " + FLAG);

                            for (int i = 0; i < BaseActivity.lectureList.size(); i++) {
                                //roomNum랑 같은 강의 리스트 가져오기
                                if (BaseActivity.lectureList.get(i).getLectureRoom().equals(roomNum)) {
                                    String lecture = BaseActivity.lectureList.get(i).getLecture();
                                    String lectureRoom = BaseActivity.lectureList.get(i).getLectureRoom();
                                    String lectureStartTime = BaseActivity.lectureList.get(i).getLectureStartTime();
                                    String lectureFinishTime = BaseActivity.lectureList.get(i).getLectureFinishTime();
                                    String lectureDay = BaseActivity.lectureList.get(i).getLectureDay();
                                    String professor =BaseActivity.lectureList.get(i).getProfessor();

                                    //roomNum에서 열리는 강의리스트를 tempList에 담기
                                    tempList.add(new Model_Lecture(lecture, lectureRoom, lectureStartTime, lectureFinishTime, lectureDay,professor));
                                }
                            }

                            for (int i = 0; i < tempList.size(); i++) {
                                for (int j = 0; j < todayList.size(); j++) {
                                    //roomNum에서 열리는 강의중 오늘 듣는 강의랑 같은이름이 있다면
                                    if (tempList.get(i).getLecture().equals(todayList.get(j).getLecture())) {
                                        try {

                                            Log.v("Hi", "여기는 됬다.");
                                            String str = switchStringminus(tempList.get(i).getLectureStartTime(), 0);//수업시작-10분
                                            String str2 = switchStringplus(tempList.get(i).getLectureStartTime(), +360);//수업시작+10분
                                            String str3 = switchStringplus(tempList.get(i).getLectureStartTime(), +540);//수업시작+30분

                                            int startTimeMinus10 = Integer.parseInt(str);
                                            int startTimePlus10 = Integer.parseInt(str2);
                                            int startTimePlus30 = Integer.parseInt(str3);
                                            int finishTime = Integer.parseInt(tempList.get(i).getLectureFinishTime());
                                            strFinishTime=tempList.get(i).getLectureFinishTime();

                                            Log.d("This startTimeMinus10", "is  " + startTimeMinus10);
                                            Log.d("This startTimePlus10", "is  " + startTimePlus10);

                                            Log.d("This nowIntTime ", "is   " + nowIntTime);
                                            Log.d("This nowDate ", "is   " + nowDate);

                                            //만약 출결테이블에 본인 학번, 본인 수강과목이 출석||지각 이 아니라면

                                            // 수업시작-10분 < 현재시간 < 수업시작+10 인 경우 ==출석
                                            if (startTimeMinus10 <= nowIntTime && nowIntTime < startTimePlus10) {
                                                postAttendance(nowTime, tempList.get(i).getLecture(), "출석", nowDate, mAudioManager, tempList.get(i).getLectureFinishTime());
                                            }
                                            // 수업시작+10 < 현재시간 < 수업시작 +30 인 경우 ==지각
                                            else if (startTimeMinus10 <= nowIntTime && nowIntTime < startTimePlus30) {
                                                postAttendance(nowTime, tempList.get(i).getLecture(), "지각", nowDate, mAudioManager, tempList.get(i).getLectureFinishTime());
                                            }
                                            // 수업시작+30 < 현재시간 < 종료시간 ==결석
                                            else if (startTimePlus30 <= nowIntTime && nowIntTime < finishTime) {
                                                postAttendance(nowTime, tempList.get(i).getLecture(), "결석", nowDate, mAudioManager, tempList.get(i).getLectureFinishTime());
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }

                    }
                    //비콘구역에 있다가 벗어났을 시
                    else {
                        silentFLAG = false;
                        if (soundSet == 0) {
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        } else if (soundSet == 1) {
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        } else if (soundSet == 2) {
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }

                }

                //연결이 없다면.
                else {
                    soundSet = mAudioManager.getRingerMode();
                }
            }
        });
        region2 = new Region("ranged region", UUID.fromString("11111111-1111-1111-1111-111111111404"), 4660, 64001);
        region = new Region("ranged region", UUID.fromString("11111111-1111-1111-1111-111111111301"), 4660, 64001);

        ////////////////////////////여기까지 비콘엑티비티////////////////////////////////////////////



        Context context = getApplicationContext();
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        soundSet = mAudioManager.getRingerMode();




        //이부분 에러각
        rv_lecuture = (RecyclerView) findViewById(R.id.lectureRecycler);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter_lecture = new Adapter_Lecture(getApplicationContext(), adapterList);
        rv_lecuture.setLayoutManager(manager);
        rv_lecuture.setAdapter(adapter_lecture);


    }
//////////////////////////////////온크리에이트//////////////////////////////////////////////////////






    public String switchStringplus(String time, int plus) throws ParseException {
        String oldstring = time;
        Date date = new SimpleDateFormat("HHmm").parse(oldstring);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, plus);
        String formatDate = new SimpleDateFormat("HHmm").format(cal.getTime());
        return formatDate;
    }

    public String switchStringminus(String time, int minus) throws ParseException {
        String oldstring = time;
        Date date = new SimpleDateFormat("HHmm").parse(oldstring);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minus);
        String formatDate = new SimpleDateFormat("HHmm").format(cal.getTime());
        return formatDate;
    }


    public String switchNowTime() {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("MMdd HHmm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        String nowTime = sdfNow.format(cal.getTime());

        return nowTime;
    }


    public void postAttendance(String nowTime, String thisLecture, String state, String todayDate, final AudioManager thisAudioManager, final String str) {
        //POST 하는 부분. 서버-DB 쪽이랑 최대한 변수 맞춰서 정리하자.

        checkList = new ArrayList<>();
        final String studentNum = currentSTUlist.get(0).getStudentNum();
        final String attendanceTime = nowTime;
        final String lecture = thisLecture;
        final String attendanceState = state;
        final String day = todayDate;
        Log.d("HI", "포스트부분");


        //FLAG = true;

        //showNotification("M4M", "강의 중 무음모드 작동");
        setSilent(thisAudioManager);


        Retrofit client = new Retrofit.Builder().baseUrl(Constants.req_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitService service = client.create(RetrofitService.class);

        Call<CheckVO> call = service.getCheckData();
        call.enqueue(new Callback<CheckVO>() {
            @Override
            public void onResponse(Call<CheckVO> call, Response<CheckVO> response) {
                if (response.isSuccessful()) {
                    repo = response.body();
                    checkTempList = repo.getList();

                    for (int i = 0; i < checkTempList.size(); i++) {
                        if (checkTempList.get(i).getStudentNum().equals(studentNum)
                                && checkTempList.get(i).getLecture().equals(lecture)
                                &&checkTempList.get(i).getAttendanceDay().equals(day))
                        {
                            String studentNum = checkTempList.get(i).getStudentNum();
                            String attendanceDay = checkTempList.get(i).getAttendanceDay();
                            String attendanceTime = checkTempList.get(i).getAttendanceTime();
                            String attendanceState = checkTempList.get(i).getAttendanceState();
                            String lecture = checkTempList.get(i).getLecture();

                            checkList.add(new Model_Check(studentNum, attendanceDay, attendanceTime, attendanceState, lecture));
                        }
                    }
                }

                    AndroidNetworking.post("http://13.124.87.34:5000/guestcheck")
                            .addBodyParameter("studentNum", studentNum)
                            .addBodyParameter("attendanceTime", attendanceTime)
                            .addBodyParameter("lecture", lecture)
                            .addBodyParameter("attendanceState", attendanceState)
                            .addBodyParameter("day", day)
                            .addHeaders("Content-Type", "multipart/form-data")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onResponse(JSONObject response) {
                                    showNotification("아맞다", lecture+" 출석 완료. 매너있게 변경되었습니다.");
                                    Log.d("지금시간확인", ": " + str);
                                }

                                @Override
                                public void onError(ANError anError) {
                                }
                            });


            }
            @Override
            public void onFailure(Call<CheckVO> call, Throwable t) {
            }
        });


    }


    public void setSilent(AudioManager mAudioManager) {
 /*       if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) //2
        {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) //0
        {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) //1
        {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }*/
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        silentFLAG = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(
                this, 0, new Intent[]{notifyIntent}
                , PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setTicker("[아맞다] 모드가 변경되었습니다")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        navigation.setSelectedItemId(R.id.navigation_home);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
                beaconManager.startRanging(region2);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }










}
