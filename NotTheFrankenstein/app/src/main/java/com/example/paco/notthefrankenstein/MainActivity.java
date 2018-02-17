package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
//Navbar activity

    public static final String HOME_TAG = "home";
    public static final String FRIENDS_TAG = "friends";
    public static final String SETTINGS_TAG = "settings";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    removeFragment();
                    addHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    removeFragment();
                    addFriendsFragment();
                    return true;
                case R.id.navigation_notifications:
                    removeFragment();
                    addSettingsFragment();
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


        /*//SIGN OUT BUTTON BINDING AND LAUNCH BACK Login.class
        signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DISCONNECTS USER
                isLoggedOut = true;
                //disconnectUser();
                FirebaseAuth.getInstance().signOut();

                //LAUNCHES BACK TO LOG IN SCREEN
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });*/

        addHomeFragment();
    }

    public void addHomeFragment(){
        HomeFragment fragment =  new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment, HOME_TAG)
                .commit();
    }

    public void addFriendsFragment(){
        FriendsFragment fragment = new FriendsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment, FRIENDS_TAG)
                .commit();
    }

    public void addSettingsFragment(){
        SettingsFragment fragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment, SETTINGS_TAG)
                .commit();
    }

    public void removeFragment(){
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }
}
