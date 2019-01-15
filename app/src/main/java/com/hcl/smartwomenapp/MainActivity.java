package com.hcl.smartwomenapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.SharedPreferences.Editor;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    Button login;
    TextView register;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(loginHandler);

        register = (TextView) findViewById(R.id.link_to_register);
        register.setOnClickListener(registerHandler);


    }


    View.OnClickListener loginHandler = new View.OnClickListener() {
        public void onClick(View v) {

            EditText username = (EditText) findViewById(R.id.login_username);
            EditText password = (EditText) findViewById(R.id.login_password);

            System.out.print(username.getText() + "," + password.getText());
            URL url;
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://serene-taiga-67213.herokuapp.com/login?" +
                            "username=" + username.getText() + "&password=" + password.getText());

                    urlConnection = (HttpURLConnection) url
                            .openConnection();
                    InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
                    BufferedReader buff = new BufferedReader(in);

                    if (buff.readLine().equals("success")) {
                        pref = getApplicationContext().getSharedPreferences("SessionPerson", 0);
                        Editor editor = pref.edit();
                        editor.putString("username", username.getText().toString()).commit();
                        Intent i = new Intent(getApplicationContext(), ChooseActivity.class);
                        startActivity(i);
                    } else {
                        createAlert("Sorry Login Failed Please try again");
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
    };
    View.OnClickListener registerHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
            // it was the 1st button
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
