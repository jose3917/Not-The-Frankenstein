package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    Button findButton;
    EditText txt;

    final static String DATA = "DATASNAPSHOT";
    final static String USERNAME = "USERNAME";
    final static String UID = "UID";
    final static String EMAIL = "EMAIL";

    HashMap<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findButton = (Button) findViewById(R.id.find_button);
        txt = (EditText) findViewById(R.id.editText);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mDatabase.getReference();


        users = new HashMap<>();
        getUsersHT();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("USER", "name: " + key + "\tuid: " + value);
        }

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTableToMaps();
            }
        });

    }

    public void getUsersHT(){
        Query q = mRef.child("Users");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    User u = dataSnapshot.getValue(User.class);
                    users.put(u.getUser_name(), u.getUid());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendTableToMaps(){
        Intent sendTable = new Intent(UserProfileActivity.this, MapsActivity.class);
        sendTable.putExtra("table", users); //HashMap
        sendTable.putExtra("string", txt.getText().toString()); //Text from box, if any
        startActivity(sendTable);
    }


    //Could be used in Settings Activity, not very helpful here
    public void getCurrentUser(){
        String current_uid = mAuth.getUid();
        Query q = mRef.child("Users").child(current_uid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d(DATA, "onDataChange(): "+dataSnapshot.getValue());
                Log.d(USERNAME, "Username: "+user.getUser_name());
                Log.d(UID, "Uid: "+user.getUid());
                Log.d(EMAIL, "Email: "+user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void refreshTable(){
        //Called in onResume when this activity is started again
        //
    }

}
