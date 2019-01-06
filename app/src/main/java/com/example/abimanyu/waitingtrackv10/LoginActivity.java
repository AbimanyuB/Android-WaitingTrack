package com.example.abimanyu.waitingtrackv10;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import mMaps.TrackGPS;
import mRequest.LoginRequest;

import static android.media.CamcorderProfile.get;

public class LoginActivity extends Activity {
    private TrackGPS gps;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Login");
        setContentView(R.layout.activity_login);

        TextView create = (TextView) findViewById(R.id.create);
        TextView getStarted = (TextView) findViewById(R.id.getstarted);
        TextView waitingTrack = (TextView) findViewById(R.id.waitingTrack);
        final EditText etPassword  = (EditText) findViewById(R.id.password);
        final EditText etUsername =  (EditText) findViewById(R.id.username);

        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "font/ArgonPERSONAL-Regular.otf");
        waitingTrack.setTypeface(custom_fonts);

        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        getStarted.setTypeface(tf);
        create.setTypeface(tf);
        etPassword.setTypeface(tf);
        etUsername.setTypeface(tf);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                String latlng = null;
                gps = new TrackGPS(LoginActivity.this);

                Calendar c = Calendar.getInstance();
                int hour =  c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int day =  c.get(Calendar.DAY_OF_MONTH);
                int date = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                time = hour + ":" + minute  + " " + day + "/" + (date+1) + "/" + year;

                if (gps.canGetLocation()) {
                    latlng = gps.getLatitude() + "," + gps.getLongitude();
                } else
                    gps.showSettingsAlert();
                if (!latlng.equals("0.0,0.0")) {
                    Response.Listener<String> responListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (!(username.trim().equals("")) && !(password.trim().equals(""))) {
                                    if (success) {

                                        SharedPreferences.Editor editor = getSharedPreferences("LOGGEDIN_SHARED_PREF", MODE_PRIVATE).edit();
                                        editor.putBoolean("status", true);
                                        editor.putString("UNAME_SHARED_PREF", username);
                                        editor.commit();

                                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Login Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } else
                                    Toast.makeText(LoginActivity.this, "Please Input All Field", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(username, password, latlng, time, responListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                } else
                    Toast.makeText(getApplicationContext(),"Your Location is Null",Toast.LENGTH_LONG).show();
            }
        });
    }
}
