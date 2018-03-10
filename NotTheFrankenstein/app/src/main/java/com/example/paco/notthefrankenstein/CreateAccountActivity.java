package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    //only two buttons
    private Button cancel;
    private Button create;

    //Strings
    String username;
    String email;
    String password;

    //Edit text fields
    private EditText edit_display;
    private EditText edit_email;
    private EditText edit_password;

    //In case user decides not to make account
    //for whatever reason


    //FireBase stuff
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "FB_SIGNIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        cancel = (Button) findViewById(R.id.btnCancel);
        create = (Button) findViewById(R.id.btnCreate2);

        final Intent cancelIntent = new Intent(this, LoginActivity.class);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Call intent
                startActivity(cancelIntent);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edit_display.getText().toString();
                email = edit_email.getText().toString();
                password = edit_password.getText().toString();
                createAccount();
            }
        });

        //SETS THE INTENT TO LAUNCH TO
        final Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //CONNECTS CURRENT USER AND LAUNCHES NEW INTENT
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){

                    Log.d(TAG, "Signed in: " + user.getUid());
                    startActivity(intent);
                    finish();

                }else{

                    Log.d(TAG, "Currenty signed out");

                }

            }
        };

    }

    public void createAccount(){
        if(!checkFormFields()) return;

        //CHECKS IF USER WAS SUCCESSFULLY CREATED
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(CreateAccountActivity.this,"com.example.kevin.mapproject.User created", Toast.LENGTH_SHORT)
                                            .show();

                                    String userId = mAuth.getCurrentUser().getUid();
                                    writeNewUser(userId, username, email);

                                }else{

                                    Toast.makeText(CreateAccountActivity.this,"Account creation failed :(", Toast.LENGTH_SHORT)
                                            .show();

                                }
                                updateStatus();

                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }

                    }
                });

    }

    private boolean checkFormFields() {
        String displayName, email, password;

        displayName = edit_display.getText().toString();
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();

        if(displayName.isEmpty()){
            edit_display.setError("Display name required");
        }
        if (email.isEmpty()) {
            edit_email.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            edit_password.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        TextView tvStat = (TextView)findViewById(R.id.tvSignInStatus);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvStat.setText("Signed in: " + user.getEmail());
        }
        else {
            tvStat.setText("Signed Out");
        }
    }

    private void updateStatus(String stat) {
        TextView tvStat = (TextView)findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
