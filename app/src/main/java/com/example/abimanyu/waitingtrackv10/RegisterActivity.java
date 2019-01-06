package com.example.abimanyu.waitingtrackv10;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import mRequest.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView signIn = (TextView) findViewById(R.id.signin);
        TextView signUp = (TextView) findViewById(R.id.signUp);
        TextView waitingTrack = (TextView) findViewById(R.id.waitingTrack2);
        final EditText passReg = (EditText) findViewById(R.id.passReg);
        final EditText userReg = (EditText) findViewById(R.id.userReg);
        final EditText nameReg = (EditText) findViewById(R.id.nameReg);
        final EditText phoneReg = (EditText) findViewById(R.id.phoneReg);

        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "font/ArgonPERSONAL-Regular.otf");
        waitingTrack.setTypeface(custom_fonts);

        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Lato-Regular.ttf");
        passReg.setTypeface(tf);
        nameReg.setTypeface(tf);
        userReg.setTypeface(tf);
        phoneReg.setTypeface(tf);
        signIn.setTypeface(tf);
        signUp.setTypeface(tf);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameReg.getText().toString();
                final String username = userReg.getText().toString();
                final String password = passReg.getText().toString();
                final String phone = phoneReg.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(!(username.trim().equals("")) && !(name.trim().equals("")) && !(password.trim().equals("")) && !(phone.trim().equals(""))) {
                                if(success){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                              }
                            } else{
                                Toast.makeText(RegisterActivity.this, "Please Input All Field", Toast.LENGTH_SHORT).show();
                              }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, name, password, phone, responseListener);
                    RequestQueue que = Volley.newRequestQueue(RegisterActivity.this);
                    que.add(registerRequest);
            }
        });
    }
}
