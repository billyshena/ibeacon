/*
 * Copyright 2014 Easi Technologies and Consulting Services, S.L.
 *  
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.easibeacon.examples.shop;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easibeacon.examples.shop.protocol.IBeacon;
import com.easibeacon.examples.shop.protocol.IBeaconListener;
import com.easibeacon.examples.shop.protocol.IBeaconProtocol;
import com.easibeacon.examples.shop.util.OffersArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements IBeaconListener{
	
	public static final int REQUEST_BLUETOOTH_ENABLE = 1;	
    
	private IBeaconProtocol ibp;
	private OffersArrayAdapter arrayAdapter;
	private static ArrayList<Offer> _offers;
	private GPSTracker gps;
	private ProgressBar _barSearchBeacons;
	private TextView _txtState;
	private float distance;
    private ListView lv;
	// Configure here your sample UUID
	public static final byte[] UUID = {(byte)0xA7,(byte)0xAE,(byte)0x2E,(byte)0xB7,(byte)0x1F,(byte)0x00,(byte)0x41,(byte)0x68,(byte)0xB9,(byte)0x9B,(byte)0xA7,(byte)0x49,(byte)0xBA,(byte)0xC1,(byte)0xCA,(byte)0x64};
    public static final byte[] AUUID = {(byte)0xB1,(byte)0x6D,(byte)0x55,(byte)0xF9,(byte)0x60,(byte)0x04,(byte)0x4A,(byte)0x6F,(byte)0xA5,(byte)0x53,(byte)0x98,(byte)0x68,(byte)0xB3,(byte)0xD7,(byte)0xE7,(byte)0x76};
    public static final byte[] BUUID = {(byte)0x46,(byte)0x6A,(byte)0x84,(byte)0x0C,(byte)0xF2,(byte)0x6D,(byte)0x41,(byte)0x2C,(byte)0x86,(byte)0x64,(byte)0xC3,(byte)0xB2,(byte)0x57,(byte)0x7F,(byte)0xF4,(byte)0x75};
    public static final byte[] CUUID = {(byte)0xB7,(byte)0x25,(byte)0xBF,(byte)0xBF,(byte)0xF8,(byte)0x07,(byte)0x4B,(byte)0xC2,(byte)0x88,(byte)0x67,(byte)0x8E,(byte)0x09,(byte)0xBF,(byte)0xBC,(byte)0x66,(byte)0x6F};
    public static final byte[] DUUID = {(byte)0x21,(byte)0x19,(byte)0xE9,(byte)0xA3,(byte)0xA1,(byte)0x37,(byte)0x46,(byte)0xD7,(byte)0xAD,(byte)0xC9,(byte)0xDA,(byte)0x88,(byte)0xFB,(byte)0x24,(byte)0x2E,(byte)0x8B};
	// Configure the UUID, major an minor of your sample easiBeacons 
	private IBeacon _sampleIBeacon1 = new IBeacon(BUUID, 1, 1);
	private IBeacon _sampleIBeacon2 = new IBeacon(BUUID, 2, 1);
	private IBeacon _sampleIBeacon3 = new IBeacon(BUUID, 3, 1);
    private IBeacon _sampleIBeacon4 = new IBeacon(BUUID, 4, 1);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);

		if(_offers == null)
			_offers = new ArrayList<Offer>();

		arrayAdapter = new OffersArrayAdapter(
                this, 
                _offers);
		ListView list = (ListView) findViewById(R.id.cardListView);
		list.setAdapter(arrayAdapter);
		
		_barSearchBeacons = (ProgressBar) findViewById(R.id.barSearchBeacons);
		_txtState = (TextView) findViewById(R.id.txtState);
		
		ibp = IBeaconProtocol.getInstance(this);
		ibp.setListener(this);
		
		TimerTask searchIbeaconTask = new TimerTask() {	
			@Override
			public void run() {
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						scanBeacons();
					}
				});
			}
		};	
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(searchIbeaconTask, 1000, 5000);
		
	}

	@Override
	protected void onStop() {
		ibp.stopScan();
		super.onStop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId() == R.id.mnuScan){
			scanBeacons();
		}
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void scanBeacons(){
		// Check Bluetooth every time

		ibp = IBeaconProtocol.getInstance(this);
		
		// Filter based on default easiBeacon UUID, remove if not required
		ibp.setScanUUID(BUUID);

		if(!IBeaconProtocol.configureBluetoothAdapter(this)){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE );
		}else{
			ibp.setListener(this);
			if(ibp.isScanning())
				ibp.scanIBeacons(false);
			ibp.reset();
			ibp.scanIBeacons(true);		
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_BLUETOOTH_ENABLE){
			if(resultCode == Activity.RESULT_OK){
				scanBeacons();
			}
		}
	}
	
	
	@Override
	public void searchState(final int state) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(state == IBeaconProtocol.SEARCH_STARTED){
					_barSearchBeacons.setVisibility(View.VISIBLE);
					_txtState.setText(R.string.msg_searching_ibeacons);
				}else if (state == IBeaconProtocol.SEARCH_END_SUCCESS){
					_barSearchBeacons.setVisibility(View.GONE);
					_txtState.setText("");
					// Will enter region, probably
				}else if (state == IBeaconProtocol.SEARCH_END_EMPTY){
					_barSearchBeacons.setVisibility(View.GONE);
					_txtState.setText(R.string.msg_no_ibeacons_found);
					//_offers.clear();
					arrayAdapter.notifyDataSetChanged();
                    Log.i("NOTHING", "FOUND");


                    // Get the JSON file as a String
                    String json = loadJSONFromAsset();
                    // create class object
                    gps = new GPSTracker(MainActivity.this);

                    lv = (ListView) findViewById(R.id.listView);

                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        //Print JSON Object
                        try {
                            List<String> results = new ArrayList<String>();
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("monuments");
                            Location myPosition = new Location("a");
                            myPosition.setLatitude(latitude);
                            myPosition.setLongitude(longitude);
                            Location monumentLoc = new Location("b");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject monumentObject = jsonArray.getJSONObject(i);
                                monumentLoc.setLatitude(monumentObject.getDouble("latitude"));
                                monumentLoc.setLongitude(monumentObject.getDouble("longitude"));
                                distance = myPosition.distanceTo(monumentLoc);
                                results.add(monumentObject.getInt("id") + "." + monumentObject.getString("name") + " " + "(" + distance / 1000 + "km )");
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    android.R.layout.simple_list_item_1,
                                    results );
                            lv.setAdapter(arrayAdapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    // TODO: CALL THE MapActivity
                                    String itemName = (String) lv.getItemAtPosition(i);
                                    Intent map = new Intent(getApplicationContext(), MapsActivity.class);
                                    map.putExtra("id", itemName);
                                    startActivity(map);
                                }
                            });
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + json);
                        }
                    }
                    else{
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }


                }
			}
		});
	}

	@Override
	public void enterRegion(IBeacon ibeacon) {
		Log.i("Shop","Enter region: " + ibeacon.toString());
		_offers.clear();
		if(ibeacon.isSameRegionAs(_sampleIBeacon1)){
            createNotification("Tour eiffel", "Voir plus d'informations", "1");
			//_offers.add(Offer.getSampleOffer1());
		}else if(ibeacon.isSameRegionAs(_sampleIBeacon2)){
            createNotification("Musée du Louvre", "Voir plus d'informations", "2");
			//_offers.add(Offer.getSampleOffer2());
		}else if(ibeacon.isSameRegionAs(_sampleIBeacon3)){
            createNotification("Sacré coeur", "Voir plus d'informations", "3");
			//_offers.add(Offer.getSampleOffer3());
		}else if(ibeacon.isSameRegionAs(_sampleIBeacon4)) {
            createNotification("Invalides", "Voir plus d'informations", "4");
            //_offers.add(Offer.getSampleOffer3());
        }

	}
	
	@Override
	public void beaconFound(IBeacon ibeacon) {
		Log.i("Shop","iBeacon found: " + ibeacon.toString());
	}
	
	@Override
	public void exitRegion(IBeacon ibeacon) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
			}
		});
	}
	
	@Override
	public void operationError(int status) {
		Log.i("EasiShop example", "Bluetooth error: " + status);	
	}


    // Function that is reading the monuments.js JSON file
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



    public void createNotification(String title, String content, String monumentId){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent resultIntent = new Intent(getApplicationContext(), MonumentActivity.class);
        Log.i("Monument", monumentId);
        resultIntent.putExtra("id", monumentId);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(Integer.parseInt(monumentId), mBuilder.build());
        arrayAdapter.notifyDataSetChanged();
    }



}	

