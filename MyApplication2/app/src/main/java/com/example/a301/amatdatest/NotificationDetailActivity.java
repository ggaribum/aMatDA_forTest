package com.example.a301.amatdatest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NotificationDetailActivity extends AppCompatActivity {


    TextView topicView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        topicView=(TextView)findViewById(R.id.notifyTopic);
        textView=(TextView)findViewById(R.id.notfiyText);
        String topic=getIntent().getExtras().getString("topic");
        String text=getIntent().getExtras().getString("text");
        topicView.setText(topic);
        textView.setText(text);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
