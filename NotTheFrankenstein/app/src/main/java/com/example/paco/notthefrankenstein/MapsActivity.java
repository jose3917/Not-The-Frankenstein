package com.example.paco.notthefrankenstein;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        UID = i.getStringExtra("UID");

        /*
        //intent testing
        Context con = getApplicationContext();
        CharSequence text = UID;
        int duration = Toast.LENGTH_LONG;
        Toast t = Toast.makeText(con,text,duration);
        t.show();
        */

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
            FirebaseAuth.getInstance().signOut();

            //LAUNCHES BACK TO LOG IN SCREEN
            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else if(item.getItemId() == R.id.menu_settings){

            Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.menu_user_profile){

            Intent intent = new Intent(MapsActivity.this, UserProfileActivity.class);
            startActivity(intent);

        }

        return true;
    }



}
