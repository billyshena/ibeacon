package com.easibeacon.examples.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

    ImageView iv;
    Button btn;
    private GPSTracker gps;
    // Current position
    Double latitude = 0.0;
    Double longitude = 0.0;
    LatLng myLocation = null;

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

        JSONObject result = null;
        String image = "";


        // Target position
        Double tLatitude = 0.0;
        Double tLongitude = 0.0;

        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("monuments");
            result = jsonArray.getJSONObject(id);
            image = result.getString("img");
            myLocation = new LatLng(result.getDouble("latitude"), result.getDouble("longitude"));
        }catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json);
        }
        gps = new GPSTracker(MapsActivity.this);
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+ latitude + ","+longitude+"&daddr="+myLocation.latitude+","+myLocation.longitude+""));
                startActivity(intent);
            }
        });
        //LatLng myLocation = new LatLng(48.8583701,2.2944813);

        // Set the according image retrieved from the monuments.json file
        iv = (ImageView)findViewById(R.id.imageV);
        int resourceId = getResources().getIdentifier(image, "drawable", "com.easibeacon.examples.shop");
        iv.setImageResource(resourceId);
        map.addMarker(new MarkerOptions().position(myLocation).title("Marker"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));


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
