package com.example.abimanyu.waitingtrackv10;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Locale;

import mRequest.FriendRequest;
import mRequest.UserRequest;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.MODE_PRIVATE;

public class FriendFragment extends Fragment {

    private ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList list_uName = new ArrayList<HashMap<String, String>>();
    ArrayList list_user = new ArrayList<HashMap<String, String>>();
    TabLayout tabLayout;
    TextView tvAddFriend;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Search EditText
        final EditText inputSearch;
        lv = (ListView) view.findViewById(R.id.lvFriend);
        inputSearch = (EditText) view.findViewById(R.id.inputFriend);
        tvAddFriend = (TextView) view.findViewById(R.id.tvAddFriend);

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

                        list_uName.add(jsonObject.getString("userfriend"));
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        SharedPreferences shpr = getContext().getSharedPreferences("LOGGEDIN_SHARED_PREF", MODE_PRIVATE);
        String username = shpr.getString("UNAME_SHARED_PREF",null);

        FriendRequest friendRequest = new FriendRequest(username, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FriendFragment.this.getContext());
        queue.add(friendRequest);

        /* tabLayout.getSelectedTabPosition();
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();

               TabHost host = (TabHost) getActivity().findViewById(android.R.id.tabhost);
                host.setCurrentTab(2);*/


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String friend = list_uName.get(position).toString();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("USER_SHARED_PREF",MODE_PRIVATE).edit();
                editor.putString("FRIEND", friend);
                editor.commit();

                Toast.makeText(getActivity().getApplicationContext(), "You choose to tracking " + friend, Toast.LENGTH_LONG).show();
            }
        });

        tvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new TrackingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, newFragment);
                transaction.commit();

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
                FriendFragment.this.adapter.getFilter().filter(text);
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
        ((MenuActivity) getActivity()).setActionBarTitle("Friend");
    }
}
