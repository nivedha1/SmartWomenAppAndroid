package com.hcl.smartwomenapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

import android.app.Activity;
public class HealthActivity extends Activity {
    EditText searchTerm;
    Button search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health);

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(searchTopicHandler);
    }

    View.OnClickListener searchTopicHandler = new View.OnClickListener() {
        public void onClick(View v) {

            searchTerm = (EditText) findViewById(R.id.term);

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection urlConnection = null;

                        try {
                            TextView results = (TextView) findViewById(R.id.details);
                            URL url = new URL("http://serene-taiga-67213.herokuapp.com/health?" +
                                    "term=" + searchTerm.getText());
                             urlConnection = (HttpURLConnection) url.openConnection();
                            InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
                            BufferedReader buff = new BufferedReader(in);
                            StringBuffer sb = new StringBuffer();
                            for (String line = buff.readLine(); line != null; line = buff.readLine()) {
                                sb.append(line);                            }
                            results.setText(sb.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                };

                for (int i = 0; i <= 5; i++) {
                    handler.postDelayed(runnable, 1000 + (i * 1000));
                }
            }
        }
    };

}
