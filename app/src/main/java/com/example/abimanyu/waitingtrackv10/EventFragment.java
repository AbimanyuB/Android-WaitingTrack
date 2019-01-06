package com.example.abimanyu.waitingtrackv10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EventFragment extends Fragment {
    String destination;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //final EditText desLoc = (EditText) view.findViewById(R.id.desLoc);
        TextView getStarted = (TextView) view.findViewById(R.id.tvGetStarted);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            //menjalankan place picker
            startActivityForResult(builder.build(EventFragment.this.getActivity()), PLACE_PICKER_REQUEST);

            // check apabila <a title="Solusi Tidak Bisa Download Google Play Services di Android" href="http://www.twoh.co/2014/11/solusi-tidak-bisa-download-google-play-services-di-android/" target="_blank">Google Play Services tidak terinstall</a> di HP
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
                //destination = desLoc.getText().toString().trim();
                //Toast.makeText(getContext().getApplicationContext(), destination, Toast.LENGTH_LONG).show();

          }
       });
    }

public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // menangkap hasil balikan dari Place Picker, dan menampilkannya pada TextView
        if (requestCode == PLACE_PICKER_REQUEST) {
        if (resultCode == RESULT_OK) {
        Place place = PlacePicker.getPlace(this.getActivity(), data);
        destination = place.getLatLng().latitude+","+place.getLatLng().longitude;
            SharedPreferences.Editor shpr = EventFragment.this.getActivity().getSharedPreferences("USER_SHARED_PREF", Context.MODE_PRIVATE).edit();
            shpr.putString("DESTINATION", destination);
            shpr.commit();
        }
        }
    }

    public void onResume() {
        super.onResume();
        // Set title bar
        ((MenuActivity) getActivity()).setActionBarTitle("Tracking");
    }
}