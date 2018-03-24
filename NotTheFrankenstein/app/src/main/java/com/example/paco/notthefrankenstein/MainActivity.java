package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private final Fragment home = new HomeFragment();
    private final Fragment friend = new FriendsFragment();
    private final Fragment settings = new SettingsFragment();
    private Fragment active = home;

    //Navbar activity
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();



            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.fragment_container, home).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.fragment_container, friend).commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.fragment_container, settings).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        
        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragment_container, settings, "3").commit();
        fm.beginTransaction().add(R.id.fragment_container, friend, "2").commit();
        fm.beginTransaction().add(R.id.fragment_container, home, "1").commit();

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
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

        return true;
    }
}
