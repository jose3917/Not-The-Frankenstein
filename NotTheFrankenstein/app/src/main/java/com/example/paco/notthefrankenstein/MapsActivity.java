package com.example.paco.notthefrankenstein;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    //End of implements

    private GoogleMap mMap;
    private String UID;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private Boolean isLoggedOut = false;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //List of users and their uids
    HashMap<String, String>  users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Creating a reference to signed-in user
        //and the database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Used to zoom into map on startup
        //Unsure if this'll work with newly created accounts
        retrieveLastKnownLocation();

        //users = new HashMap<>();

        //SAVING PREFERENCES
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }

    public void retrieveLastKnownLocation(){
        DatabaseReference l = mDatabase.child("Locations");
        GeoFire geoFire = new GeoFire(l);
        geoFire.getLocation(mAuth.getUid(), new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if(location!=null){
                    LatLng lt = new LatLng(location.latitude, location.longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lt));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        DatabaseReference ref = mDatabase.child("Locations");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(mAuth.getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()));

    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.setting_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.log_out_menu_item){
            //disconnectUser();
            mAuth.signOut();

            //LAUNCHES BACK TO LOG IN SCREEN
            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);

        }else if(item.getItemId() == R.id.menu_settings){

            Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.menu_user_profile){

            Intent intent = new Intent(MapsActivity.this, UserProfileActivity.class);
            startActivity(intent);

        }

        return true;
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        //maybe change so that it only disconnects when user logs out
        //can still change location in other activities if still signed in
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("RESUME", "Hello!");
        Intent intent = getIntent();
        if(intent.getSerializableExtra("table")!=null){
            //old updateHashMap((HashMap<String, String>) intent.getSerializableExtra("table"));
            Serializable data = getIntent().getSerializableExtra("table");
            Log.d("RESUME", "Received hash table, boi");
            updateHashMap(receiveTable());
            if(!intent.getStringExtra("string").isEmpty()){
                Log.d("RESUME", "Got the string too, it's: "
                        +intent.getStringExtra("string"));
                String s = intent.getStringExtra("string");
                findPerson(s);
                logHashMap();
            }
            else{
                Log.d("RESUME", "Empty string, bitch");
                logHashMap();
            }
        }
        else{
            Log.d("MISSING", "Hashtable not found.");
        }
    }

    //necessary for retrieving hash table
    public HashMap<String, String> receiveTable(){
        Intent receiveTable = getIntent();
        return (HashMap<String, String>) receiveTable.getSerializableExtra("table");
    }

    public void findPerson(String s){ //Places marker on map
        if(users.containsKey(s)){ //Case sensitive, uses username(key) to get UID(value)
            Log.d("SEARCH_SUCCESS", "Found a user! It's "+s+" c(**c)");
            //Create references to GeoFire and Locations table
            DatabaseReference l = mDatabase.child("Locations");
            GeoFire person = new GeoFire(l);

            //Username re-initialized, can't use 's' in onLocationResult unless it's final
            final String username = s;

            //Get username's UID
            String uid = users.get(s);

            person.getLocation(uid, new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    if(location != null){
                        LatLng lt = new LatLng(location.latitude, location.longitude);
                        mMap.addMarker(new MarkerOptions().position(lt).title(username));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lt));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //person.getLocation();
        }
        else{ //User doesn't exist, or empty string
            Log.d("SEARCH_FAIL", "Didn't find a user!");
        }
    }

    public void updateHashMap(HashMap<String, String> hashMap){
        if(users != hashMap || users.isEmpty()){
            users = hashMap;
        }
    }

    public void logHashMap(){
        if(!users.isEmpty()) {
            Log.d("LOG_MAP", "Showing users...");
            for (Map.Entry<String, String> entry : users.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d("USER", "name: " + key + "\tuid: " + value);
            }
        }
    }

}
