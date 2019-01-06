package com.example.abimanyu.waitingtrackv10;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mMaps.DirectionFinder;
import mMaps.DirectionFinderListener;
import mMaps.Route;
import mMaps.TrackGPS;
import mRequest.AddFriendRequest;
import mRequest.ProfileRequest;
import mRequest.UserRequest;

import static android.content.Context.MODE_PRIVATE;

public class TrackingFragment extends Fragment {

    private ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList list_uName = new ArrayList<HashMap<String, String>>();
    ArrayList list_user = new ArrayList<HashMap<String, String>>();
    TabLayout tabLayout;
    String username;

    public TrackingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tracking, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Search EditText
        final EditText inputSearch;
        lv = (ListView) view.findViewById(R.id.list_view);
        inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        tabLayout = (TabLayout) view.findViewById(R.id.bottom_navigation);

        adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, list_uName);
        lv.setAdapter(adapter);
        // ArrayList for Listview
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray data = jsonResponse.getJSONArray("user");
                    for(int i=0;i<data.length();i++){
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = data.getJSONObject(i);

                        list_uName.add(jsonObject.getString("username"));
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UserRequest userRequest = new UserRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(TrackingFragment.this.getContext());
        queue.add(userRequest);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                SharedPreferences shpr = getContext().getSharedPreferences("LOGGEDIN_SHARED_PREF", Context.MODE_PRIVATE);
                username = shpr.getString("UNAME_SHARED_PREF",null);
                final String userfriend = list_uName.get(position).toString();
                Response.Listener<String> responseListener5 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(username.isEmpty()){
                            Toast.makeText(getContext().getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
                            }
                    }
                };

                AddFriendRequest addFriendRequest = new AddFriendRequest(username, userfriend, responseListener5);
                RequestQueue que = Volley.newRequestQueue(TrackingFragment.this.getContext());
                que.add(addFriendRequest);

               /* tabLayout.getSelectedTabPosition();
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();

               TabHost host = (TabHost) getActivity().findViewById(android.R.id.tabhost);
                host.setCurrentTab(2);*/

                Toast.makeText(getActivity().getApplicationContext(), "You choose to add " + userfriend, Toast.LENGTH_LONG).show();
            }
        });

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                TrackingFragment.this.adapter.getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }


    public void onResume(){
        super.onResume();
        // Set title bar
        ((MenuActivity) getActivity()).setActionBarTitle("Add Friend");
    }
}
