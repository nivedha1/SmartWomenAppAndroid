package com.hcl.smartwomenapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.StrictMode;
import android.content.Context;
import android.content.DialogInterface;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Button register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(registerHandler);
        TextView navLoginScreen = (TextView) findViewById(R.id.link_to_login);
        navLoginScreen.setOnClickListener(navLoginScreenHandler);
    }
    View.OnClickListener navLoginScreenHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

    };

    View.OnClickListener registerHandler = new View.OnClickListener() {
        public void onClick(View v) {

            EditText name = (EditText) findViewById(R.id.reg_fullname);
            EditText username = (EditText) findViewById(R.id.reg_username);
            EditText password = (EditText) findViewById(R.id.reg_password);

            if (password.getText().length() < 6) {
                createAlert("Password must be atleast 6 digits",false);
            } else {
                System.out.print(name.getText() + "," + username.getText() + "," + password.getText());
                URL url;
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL("http://serene-taiga-67213.herokuapp.com/createuser?" +
                                "name=" + name.getText() + "&username=" + username.getText() + "&password=" + password.getText());

                        urlConnection = (HttpURLConnection) url
                                .openConnection();
                        InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
                        BufferedReader buff = new BufferedReader(in);
                        String line;
                        do {
                            line = buff.readLine();
                            System.out.print(line);
                            if (line.equals("success")) {
                                createAlert("You are registered! Please log in",true);
                            } else {
                                createAlert("Sorry Registration Failed Please try again.User Name already exists!",false);
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
    public void createAlert(String message, final boolean changeActivity)
    {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        if(changeActivity){
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }
                    }
                });


        AlertDialog alert = builder.create();
        alert.show();
    }


}

