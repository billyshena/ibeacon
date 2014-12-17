package com.easibeacon.examples.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MonumentActivity extends Activity {

    ImageView img;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monument_activity);
        Bundle extras = getIntent().getExtras();
        img = (ImageView)findViewById(R.id.imageView);
        txt = (TextView)findViewById(R.id.textView);
        //img.setImageResource(R.drawable.pariss);
        int id = Integer.parseInt(extras.getString("id")) - 1;

        // Get the JSON file as a String
        String json = loadJSONFromAsset();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("monuments");
            JSONObject result = jsonArray.getJSONObject(id);

            txt.setText(result.getString("content"));
            if(id == 0){
                img.setImageResource(R.drawable.pariss);
            }
            else if(id == 1){
                img.setImageResource(R.drawable.louvre);
            }
            else if(id == 2){
                img.setImageResource(R.drawable.sacre);
            }
            else if(id == 3){
                img.setImageResource(R.drawable.invalides);
            }
        }catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json);
        }

        Toast.makeText(getApplicationContext(), "MonumentActivity" + extras.getString("id"), Toast.LENGTH_SHORT).show();

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