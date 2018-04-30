package com.example.paco.notthefrankenstein;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity {

    Button addFriendBtn;
    EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend2);

        addFriendBtn = (Button) findViewById(R.id.look_up_button);
        mText = (EditText) findViewById(R.id.search_bar);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String test = mText.getText().toString();

                HashMap<String,String> myHash = new HashMap<String,String>();

                myHash.put("first", test);

                sendPushToSingleInstance(getApplicationContext(), myHash, FirebaseInstanceId.getInstance().getToken());
            }
        });

    }

    public static void sendPushToSingleInstance(final Context activity,
                                                final HashMap dataValue /*your data from the activity*/,
                                                final String instanceIdToken
            /*firebase instance token you will find in documentation that how to get this*/ ) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable();
                rawParameters.put("data", new JSONObject(dataValue));
                rawParameters.put("to", instanceIdToken);
                return new JSONObject(rawParameters).toString().getBytes();
            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AIzaSyD4uBA6dC7-3b_vKTn3PzchQTf6iP1NBmA" );
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }
}
