package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    Button findButton;
    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        findButton = (Button) findViewById(R.id.find_button);
        txt = (EditText) findViewById(R.id.editText);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, MapsActivity.class);
                String s = String.valueOf(txt.getText());
                if(s.isEmpty()){
                    txt.setError("Please enter something");
                }
                else {
                    intent.putExtra("UID", s);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
