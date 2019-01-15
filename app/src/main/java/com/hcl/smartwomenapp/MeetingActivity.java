package com.hcl.smartwomenapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nivedharajaram on 3/29/17.
 */

public class MeetingActivity extends AppCompatActivity {
    Button setupMeeting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting);

        setupMeeting = (Button) findViewById(R.id.btn_SetupMeeting);
        setupMeeting.setOnClickListener(saveMeetingDetails);

    }

    View.OnClickListener saveMeetingDetails = new View.OnClickListener() {
        public void onClick(View v) {
             CalendarView meetingdate = (CalendarView) findViewById(R.id.calendar);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
             String formattedDate = df.format(c.getTime());
            SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPerson", 0);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("date",formattedDate).commit();

            meetingdate.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {

               public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                   Calendar c = Calendar.getInstance();
                   SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                   c.set(year,month,dayOfMonth);
                   String formattedDate = df.format(c.getTime());
                   SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPerson", 0);

                   SharedPreferences.Editor editor = pref.edit();
                   editor.putString("date",formattedDate).commit();
                }//met
            });

            EditText meetingTimeFrom = (EditText) findViewById(R.id.meeting_time_from);
            EditText meetingTimeTo = (EditText) findViewById(R.id.meeting_time_to);
            EditText daycareNo = (EditText) findViewById(R.id.daycare_no);
            EditText forwardNo = (EditText) findViewById(R.id.forward_no);


            if (meetingTimeFrom.getText().toString() != null &&
                    meetingTimeFrom.getText().toString().split(":") != null &&
                    meetingTimeFrom.getText().toString().split(":").length != 2 &&
                    meetingTimeFrom.getText().toString().split(":")[0].length() != 2 ||
                    meetingTimeFrom.getText().toString().split(":")[1].length() != 2) {
                createAlert("Please enter a valid to From time");
            } else if (meetingTimeTo.getText().toString() != null &&
                    meetingTimeTo.getText().toString().split(":") != null &&
                    meetingTimeTo.getText().toString().split(":").length != 2 &&
                    meetingTimeTo.getText().toString().split(":")[0].length() != 2 ||
                    meetingTimeTo.getText().toString().split(":")[1].length() != 2) {
                createAlert("Please enter a valid to To time");
            } else if (daycareNo.getText() != null && daycareNo.getText().length() < 10) {
                createAlert("Please enter a 10 digit daycare no");
            } else if (forwardNo.getText() != null && forwardNo.getText().length() < 10) {
                createAlert("Please enter a 10 digit daycare call forward no");
            } else {

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL("http://serene-taiga-67213.herokuapp.com/addPhoneNos?" +
                                "username=" + getApplicationContext().getSharedPreferences("SessionPerson", 0).getString("username", "")+
                                "&meeting_date=" + getApplicationContext().getSharedPreferences("SessionPerson", 0).getString("date", "")+
                                "&meeting_time_from=" + meetingTimeFrom.getText() +
                                "&meeting_time_to=" + meetingTimeTo.getText() + "&daycare_no=" + daycareNo.getText() +
                                "&forward_no=" + forwardNo.getText());


                        urlConnection = (HttpURLConnection) url
                                .openConnection();
                        InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
                        BufferedReader buff = new BufferedReader(in);
                        String line;
                        do {
                            line = buff.readLine();
                            System.out.print(line);
                            if (line.equals("success")) {
                                createAlert("Success ! Details saved.");

                            } else {
                                createAlert("Sorry Numbers not saved Please try again");
                            }
                        } while (line != null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            }
        }


    };
    public void createAlert(String message) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
