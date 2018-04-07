package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void FindFriendButtonOnClick(View view) {

        Button findButton = (Button) findViewById(R.id.find_button);
        final EditText txt = (EditText) findViewById(R.id.editText);
        final String s = String.valueOf(txt.getText());

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserProfileActivity.this, MapsActivity.class);
                intent.putExtra("UID", s.toString());
                startActivity(intent);
                finish();


            }
        });


    }
}
