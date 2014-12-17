package com.easibeacon.examples.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * This is where we can add markers :=or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        // Get the JSON file as a String
        String json = loadJSONFromAsset();
        Bundle extras = getIntent().getExtras();
        int id = Integer.parseInt(extras.getString("id").substring(0,1)) - 1;
        LatLng myLocation = null;
        JSONObject result = null;
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("monuments");
            result = jsonArray.getJSONObject(id);
            myLocation = new LatLng(result.getDouble("latitude"), result.getDouble("longitude"));
        }catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json);
        }

        //LatLng myLocation = new LatLng(48.8583701,2.2944813);

        map.addMarker(new MarkerOptions().position(myLocation).title("Marker"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14));
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("monuments.js");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }



}
