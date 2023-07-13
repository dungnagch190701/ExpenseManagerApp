package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

// Implement OnMapReadyCallback.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Geocoder geocoder;
    Address address;
    GoogleMap gm;
    SupportMapFragment mapFragment;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        geocoder = new Geocoder(this);

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gm = googleMap;
        new CongViec().execute(location);




    }
    private class CongViec extends AsyncTask<String,Void,List<Address>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Address> doInBackground(String... strings) {
            String s  = strings[0];
            Log.d("NAME:",s);
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocationName(s,1);
                Log.d("A", String.valueOf(addressList));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addressList;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if ((addresses != null) && (addresses.size() > 0)) {
                address = addresses.get(0);
                Marker marker = gm.addMarker(new MarkerOptions()
                        .position(new LatLng(address.getLatitude(), address.getLongitude()))
                        .title(address.getAddressLine(0)));
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                marker.showInfoWindow();



            super.onPostExecute(addresses);
        }
    }
}
}