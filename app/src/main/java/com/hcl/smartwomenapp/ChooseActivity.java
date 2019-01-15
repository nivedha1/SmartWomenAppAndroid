package com.hcl.smartwomenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class ChooseActivity extends AppCompatActivity {

    Button meeting;
    Button health;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        meeting = (Button) findViewById(R.id.meeting_details);
        meeting.setOnClickListener(meetingHandler);

        health = (Button) findViewById(R.id.women_health);
        health.setOnClickListener(healthHandler);
    }

    View.OnClickListener meetingHandler = new View.OnClickListener() {
        public void onClick(View v) {

            Intent i = new Intent(getApplicationContext(), MeetingActivity.class);
            startActivity(i);
        }
    };
    View.OnClickListener healthHandler = new View.OnClickListener() {
        public void onClick(View v) {

            Intent i = new Intent(getApplicationContext(), HealthActivity.class);
            startActivity(i);
        }
    };
}