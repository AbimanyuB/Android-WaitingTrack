package com.example.abimanyu.waitingtrackv10;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mRequest.ProfileRequest;
import mRequest.RegisterRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final TextView nameProf = (TextView) view.findViewById(R.id.nameProf);
        final TextView userProf = (TextView) view.findViewById(R.id.userProf);
        final TextView phoneProf = (TextView) view.findViewById(R.id.phoneProf);


        SharedPreferences shpr = this.getActivity().getSharedPreferences("LOGGEDIN_SHARED_PREF", Context.MODE_PRIVATE);
        String username = shpr.getString("UNAME_SHARED_PREF", null);

        final ArrayList list_data = new ArrayList<HashMap<String, String>>();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        nameProf.setText(jsonResponse.getString("name"));
                        userProf.setText(jsonResponse.getString("username"));
                        phoneProf.setText(jsonResponse.getString("phone"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ProfileRequest profileRequest = new ProfileRequest(username, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ProfileFragment.this.getContext());
        queue.add(profileRequest);

    }

    public void onResume() {
        super.onResume();
        // Set title bar
        ((MenuActivity) getActivity()).setActionBarTitle("Profile");
    }
}
