package com.example.holykael.ontime_travellers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        setContentView(R.layout.activity_main);
    }

    public void loadSharedPreferences(){
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);

        // Values
        String email = preferences.getString("email",null);
        String password = preferences.getString("password",null);

        // Then you want to query preferences for values such as username and password like
        if((email != null) && (password != null))
        {
            // **** Log your user into your application auto**Magically** ********

            // ------------------- Option 1 - web request -------------------

            // First i Would make the initial Web Request before trying to send my User into
            // a new Activity.
            // Run an `AsynchTask` against your webservice on the server if this is something
            // you need to do to see if the username and password, are correct and registered

            //---------- Option 2 - Check the SQLite Database(if you are using one) ---------

            // Otherwise you can use this info to read from an SQLiteDatabase on your device.
            // To see if they are registered

            // This is where you would create a new intent to start
            // and Login your user, so that when your application is launched
            // it will check if there are a username and password associated to the
            // Application, if there is and these are not null do something like

            // Create a new Intent
            //   Intent automagicLoginIntent = new Intent(getBaseContext(),AutomagicLogin.class);

            // Pass the Bundle to the New Activity, if you need to reuse them to make additional calls
            //    Bundle args = new Bundle();
            //   args.putString("email", email);
            //    args.putString("password", password);

            //  automagicLoginIntent.putExtra("UserInfo", args);

            // Launch the Activity
            //startActivity(automagicLoginIntent);

        }else{
            // Show the Layout for the current Activity
        }
    }

    public void loginRequest(View view) {
        EditText emailView = (EditText) findViewById(R.id.email);
        EditText passView = (EditText) findViewById(R.id.password);

        final String email = emailView.getText().toString();
        final String pass = passView.getText().toString();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.2:80/onTimeServer/v1/login";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                Log.d("LOGIN",Response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }) {
            @Override
            public byte[] getBody()
            {
                try
                {
                    final String body = "&email=" + email + // assumes username is final and is url encoded.
                            "&password=" + pass;// assumes password is final and is url encoded.
                    return body.getBytes("utf-8");
                }
                catch (Exception ex) { }

                return null;
            }

            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Accept", "application/json");

                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        //Save login data on sharedPreferences
        /*
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
         */
    }
}
