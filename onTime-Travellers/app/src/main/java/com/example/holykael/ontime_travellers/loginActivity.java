package com.example.holykael.ontime_travellers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class loginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        installationUuidSharedPreferences();
        loadSharedPreferences();
        setContentView(R.layout.activity_main);
    }


    public void installationUuidSharedPreferences(){
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        String uuid = preferences.getString("loginUUID",null);
        SharedPreferences.Editor editor = preferences.edit();
        if(uuid==null) {
            String uniqueID = UUID.randomUUID().toString();
            editor.putString("loginUUID",uniqueID);
            editor.apply();
        }
    }
    public void saveSharedPreferences(String email){
        SharedPreferences sharedPref = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("loginEmail",email);
        editor.apply();

    }

    public void loadSharedPreferences(){
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);

        // Values
        String email = preferences.getString("loginEmail",null);
        String uuid = preferences.getString("loginUUID",null);

        // Then you want to query preferences for values such as username and password like
        if((uuid!=null) && (email != null))
        {
            Log.d("UUID",uuid);
            Log.d("email",email);
            autoLoginRequest(email,uuid,new VolleyCallback(){
                @Override
                public void onSuccess(){
                    Log.d("LOGIN","AUTO LOGIN SUCCESS");
                    Intent onTimeIntent = new Intent(loginActivity.this,onTimeActivity.class);
                    startActivity(onTimeIntent);
                }
            });
        }
    }

    public void loginClick(View view) {
        EditText emailView = (EditText) findViewById(R.id.email);
        EditText passView = (EditText) findViewById(R.id.password);

        final String email = emailView.getText().toString();
        final String pass = passView.getText().toString();
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        String uuid = preferences.getString("loginUUID",null);
        loginRequest(email,pass,uuid,new VolleyCallback(){
            @Override
            public void onSuccess(){
                saveSharedPreferences(email);
                Intent onTimeIntent = new Intent(loginActivity.this,onTimeActivity.class);
                startActivity(onTimeIntent);
            }
        });

    }

    public void loginRequest(String e,String p,String u,final VolleyCallback callback) {
        final String email=e;
        final String pass=p;
        final String uuid=u;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/login";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                Log.d("LOGIN",Response.toString());
                try {
                    if (!Response.getBoolean("error")){
                        callback.onSuccess();
                    }

                    else
                    {
                        TextView errorview =(TextView) findViewById(R.id.error);
                        errorview.setText(Response.getString("message"));
                        errorview.setVisibility(View.VISIBLE);
                    }
                }
                catch(JSONException e){
                    Log.d("JSONException","Try Login Again");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                TextView errorview = (TextView) findViewById(R.id.error);
                if(e.networkResponse!=null)
                    if( e.networkResponse.statusCode==400) {
                        errorview.setText(getString(R.string.error_400));
                        errorview.setVisibility(View.VISIBLE);
                    }
                else{
                        errorview.setText(getString(R.string.time_out));
                        errorview.setVisibility(View.VISIBLE);
                    }
            }
        }) {
            @Override
            public byte[] getBody(){
                try
                {
                    final String body = "&email=" + email +
                            "&password=" + pass +
                            "&uuid=" + uuid;
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
    }

    public void autoLoginRequest(String e,String uuid,final VolleyCallback callback) {
        final String email=e;
        final String id=uuid;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/autologin";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                Log.d("LOGIN",Response.toString());
                try {
                    if (!Response.getBoolean("error")){
                       callback.onSuccess();
                    }

                    else
                    {
                        TextView errorview =(TextView) findViewById(R.id.error);
                        errorview.setText(Response.getString("message"));
                        errorview.setVisibility(View.VISIBLE);
                    }
                }
                catch(JSONException e){
                    Log.d("JSONException","Try Login Again");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                TextView errorview = (TextView) findViewById(R.id.error);
                if(e.networkResponse!=null)
                    if( e.networkResponse.statusCode==400) {
                        errorview.setText(getString(R.string.error_400));
                        errorview.setVisibility(View.VISIBLE);
                    }
                    else{
                        errorview.setText(getString(R.string.time_out));
                        errorview.setVisibility(View.VISIBLE);
                    }
            }
        }) {
            @Override
            public byte[] getBody(){
                try
                {
                    final String body = "&email=" + email +  // assumes username is final and is url encoded.
                            "&uuid=" + id;            // assumes password is final and is url encoded.
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

    }

    public void registerClick(View view){
        Intent registerIntent = new Intent(loginActivity.this,registerActivity.class);
        startActivity(registerIntent);
    }
}

