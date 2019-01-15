package com.hcl.smartwomenapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InitActivity extends AppCompatActivity {

    private Button buttonRequestPermission;

    private int PHONE_STATE_PERMISSION_CODE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init);
        buttonRequestPermission = (Button) findViewById(R.id.buttonRequestPermission);
        buttonRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadReadPhoneAllowed()) {
                    storeNumbers();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                requestReadPhonePermission();
            }
        });
    }


    private boolean isReadReadPhoneAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }


    private void requestReadPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            createAlert("You need to grant this permission to run this application");
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PHONE_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted to read phone state", Toast.LENGTH_LONG).show();
                storeNumbers();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

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

    public void storeNumbers()
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url;

            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://serene-taiga-67213.herokuapp.com/getPhoneNos?" +
                        "username=" + getApplicationContext().getSharedPreferences("SessionPerson", 0).getString("username", ""));

                urlConnection = (HttpURLConnection) url
                        .openConnection();
                InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
                BufferedReader buff = new BufferedReader(in);
                StringBuffer buffline = new StringBuffer();

                for (String line = buff.readLine(); line != null; line = buff.readLine()) {
                    buffline.append(line);
                }
                JSONArray jsonArray = new JSONArray(buffline.toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = df.format(c.getTime());
                SimpleDateFormat dftime = new SimpleDateFormat("HH:mm");
                String formattedTime = dftime.format(c.getTime());
                if (formattedDate.equals(jsonObject.getString("meeting_date")) &&
                        formattedTime.compareTo(jsonObject.getString("meeting_time_from")) > 0 && formattedTime.compareTo(jsonObject.getString("meeting_time_to")) < 0) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPerson", 0);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("phoneno", jsonObject.getString("daycare_no")).commit();
                    editor.putString("forwardno", jsonObject.getString("forward_no")).commit();

                }


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
