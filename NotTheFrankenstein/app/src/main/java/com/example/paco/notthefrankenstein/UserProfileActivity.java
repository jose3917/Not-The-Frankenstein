package com.example.paco.notthefrankenstein;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
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

    //List of users and their uids
    HashMap<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findButton = (Button) findViewById(R.id.find_button);
        txt = (EditText) findViewById(R.id.editText);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        users = new HashMap<>();
        getUsersHT();

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTableToMaps();
            }
        });

    }

    private boolean checkFormField() {
        String s = txt.getText().toString();;

        if(s.isEmpty()){
            txt.setError("Search key required");
            return false;
        }

        return true;
    }

    public static void setUserName(String s){

        FirebaseAuth nAuth;
        FirebaseDatabase nDatabase;
        DatabaseReference nRef;

        nDatabase = FirebaseDatabase.getInstance();
        nAuth = FirebaseAuth.getInstance();
        nRef = nDatabase.getReference();

        nRef.child("Users").child(nAuth.getUid()).child("user_name").setValue(s);

    }

    public void getCurrentUserData(){
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

    public void getUsersHT(){
        Query q = mRef.child("Users");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                //Log.d(TAG, "onChildAdded(): "+user.getUid());
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    //Log.d(DATA, "onChildAdded(): "+dataSnapshot.getValue());
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
        if(!checkFormField()) return;
        Intent sendTable = new Intent(UserProfileActivity.this, MapsActivity.class);
        sendTable.putExtra("table", users);
        String field = txt.getText().toString();
        sendTable.putExtra("string", field);
        startActivity(sendTable);
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DESTROY", "done");
    }


}
