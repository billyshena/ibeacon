package com.easibeacon.examples.shop;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends Activity {

    ImageView addTrack;
    ListView lv;
    EditText et;
    String NAME = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_track_activity);
        addTrack = (ImageView) findViewById(R.id.track);
        lv = (ListView) findViewById(R.id.listView2);
        et = (EditText) findViewById(R.id.editText);
        final List<String> trackList = new ArrayList<String>();
        trackList.add("Mon parcours dans le 18ème arrondissement");
        trackList.add("Place de la Concorde");
        trackList.add("Champs Elysée");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_row, R.id.title,
                trackList );
        lv.setAdapter(arrayAdapter);

        SharedPreferences prefs = getSharedPreferences(NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);

        // onClick => add a new Track to the ListView
        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackList.add(0,et.getText().toString());
                arrayAdapter.notifyDataSetChanged();

                SharedPreferences.Editor editor = getSharedPreferences(NAME, MODE_PRIVATE).edit();
                editor.putString("name", et.getText().toString());
                editor.apply();

            }
        });
    }


}
