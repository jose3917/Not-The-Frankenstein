package com.example.paco.notthefrankenstein;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String LOG_TAG = LoginActivity.class.getSimpleName();
    private final String TAG = "FB_SIGNIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private EditText etPass;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Sign In Menu!");

        //BINDS XML BUTTON TO ON CLICKLISTENER
        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);

        //BINDS XML EDITTEXTS TO JAVA EDITTEXTS
        etEmail = (EditText)findViewById(R.id.etEmailAddr);
        etPass = (EditText)findViewById(R.id.etPassword);

        //CONNECTS TO FIREBASE?
        mAuth = FirebaseAuth.getInstance();

        //SETS THE INTENT TO LAUNCH TO
        final Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //CONNECTS CURRENT USER AND LAUNCHES NEW INTENT
        mAuthListner = new FirebaseAuth.AuthStateListener() {
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

    @Override
    public void onStart(){

        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);

    }

    @Override
    public void onStop(){

        super.onStop();
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signUserIn();
                break;

            case R.id.btnCreate:
                createUserAccount();
                break;

        }
    }

    //sign in
    private void signUserIn(){

        if(!checkFormFields()){

            return;

        }

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        //CHECKS IF LOG IN WAS SUCCESSFUL
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(LoginActivity.this,"Signed in", Toast.LENGTH_SHORT)
                                            .show();

                                }else{

                                    Toast.makeText(LoginActivity.this,"Sign in failed", Toast.LENGTH_SHORT)
                                            .show();

                                }

                                updateStatus();

                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(e instanceof FirebaseAuthInvalidCredentialsException){

                            updateStatus("Invalid password");

                        }else if(e instanceof FirebaseAuthInvalidUserException){

                            updateStatus("No account with this email");

                        }else{

                            updateStatus(e.getLocalizedMessage());

                        }

                    }
                });

    }

    //create user
    private void createUserAccount(){

        if(!checkFormFields()){

            return;

        }

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        //CHECKS IF USER WAS SUCCESSFULLY CREATED
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(LoginActivity.this,"com.example.kevin.mapproject.User created", Toast.LENGTH_SHORT)
                                            .show();

                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                    current_user_db.setValue(true);

                                }else{

                                    Toast.makeText(LoginActivity.this,"Account creation failed :(", Toast.LENGTH_SHORT)
                                            .show();

                                }

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
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            etPass.setError("Password Required");
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

    public void launchNewMainActivity(View view){

        Log.d(LOG_TAG, "Button Clicked!");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
