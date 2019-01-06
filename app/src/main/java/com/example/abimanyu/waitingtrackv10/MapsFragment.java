package com.example.abimanyu.waitingtrackv10;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mMaps.DirectionFinder;
import mMaps.DirectionFinderListener;
import mMaps.Route;
import mMaps.TrackGPS;
import mRequest.InsertLatlngRequest;
import mRequest.LatlngRequest;
import mRequest.ProfileRequest;
import mRequest.UserRequest;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {

    private MapView mapView;
    private GoogleMap mMap;
    private TextView btnFindPath;
    private EditText etDestination;
    private TextView tvDuration;
    private TextView tvDistance;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TrackGPS gps;
    private TextView tvUserMaps;
    private TextView tvTimeMaps;
    public String latLng = null;
    String latLngUpdate;
    Double Distance = null;
    android.os.Handler customHandler;
    String namaUser;
    String nama;
    SharedPreferences shpr;
    SharedPreferences shprTime;
    SharedPreferences shprUpdate;
    String origin = "";
    String destination = "";
    public String time;
    public String timeUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        btnFindPath = (TextView) view.findViewById(R.id.tvFindPath);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvUserMaps = (TextView) view.findViewById(R.id.tvUserMaps);
        tvTimeMaps = (TextView) view.findViewById(R.id.tvTimeMaps);

        gps = new TrackGPS(MapsFragment.this.getContext());

        shpr = MapsFragment.this.getActivity().getSharedPreferences("USER_SHARED_PREF", Context.MODE_PRIVATE);
        namaUser = shpr.getString("FRIEND",null);

        tvUserMaps.setText(namaUser);

        setRepeatingSendLocation();

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRepeatingGetLocation();
            }
        });
    }

    private void setRepeatingSendLocation() {

        final Handler handlerSend = new Handler();
        Timer timerSend = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handlerSend.post(new Runnable() {
                    public void run() {
                        try {
                            if (gps.canGetLocation()) {
                                gps = new TrackGPS(MapsFragment.this.getContext());

                                shprUpdate = MapsFragment.this.getActivity().getSharedPreferences("LOGGEDIN_SHARED_PREF", Context.MODE_PRIVATE);
                                nama = shprUpdate.getString("UNAME_SHARED_PREF", null);

                                Calendar c = Calendar.getInstance();
                                int hour =  c.get(Calendar.HOUR_OF_DAY);
                                int minute = c.get(Calendar.MINUTE);

                                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                                timeUpdate  = hour + ":" + minute + " " + df.format(c.getTime());

                                latLngUpdate = gps.getLatitude() + "," +  gps.getLongitude();

                                Response.Listener<String> responseListenerUpdate = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                //Toast.makeText(getContext().getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                InsertLatlngRequest insertlatlngRequest = new InsertLatlngRequest(nama, latLngUpdate, timeUpdate, responseListenerUpdate);
                                RequestQueue que = Volley.newRequestQueue(MapsFragment.this.getContext());
                                que.add(insertlatlngRequest);

                            } else {gps.showSettingsAlert();}

                        }catch (Exception e) {
                        }
                    }
                });
            }
        };
        timerSend.schedule(task, 0, 60 * 150);  // interval of one minute
    }

    private void setRepeatingGetLocation() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            latLng = (jsonResponse.getString("latlng"));
                                            time = (jsonResponse.getString("time"));
                                            tvTimeMaps.setText(time);
                                            sendRequest();
                                            //Toast.makeText(getContext().getApplicationContext(),"Update Cuy!",Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(getContext().getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            LatlngRequest latlngRequest = new LatlngRequest(namaUser, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(MapsFragment.this.getContext());
                            queue.add(latlngRequest);

                        }catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60 * 150);  // interval of ten seconds
    }

    private void sendRequest() {
        shpr = MapsFragment.this.getActivity().getSharedPreferences("USER_SHARED_PREF", Context.MODE_PRIVATE);
        origin = latLng;
        destination = shpr.getString("DESTINATION", null);
        //Toast.makeText(MapsFragment.this.getContext(), destination, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MapsFragment.this.getContext(),origin, Toast.LENGTH_SHORT).show();

        if (origin.equals("")) {
            Toast.makeText(MapsFragment.this.getContext(), "Please Choose Your Meeting Location!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.equals("")) {
            Toast.makeText(MapsFragment.this.getContext(), "Please Choose Your Friend!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LatLng curLoc = new LatLng(gps.getLatitude(), gps.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(curLoc, 15);
        mMap.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    public void onDirectionFinderStart() {
       // progressDialog = ProgressDialog.show(this.getContext(), "Please wait...",
               // "Finding direction...!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
       // progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            tvDuration.setText(route.duration.text);
            tvDistance.setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.parseColor("#03A9F4")).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    public void onResume(){
        super.onResume();
        // Set title bar
        ((MenuActivity) getActivity()).setActionBarTitle("Maps");
    }
}
