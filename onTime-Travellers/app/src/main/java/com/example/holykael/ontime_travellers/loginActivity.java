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

public class loginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        loadSharedPreferences();
        setContentView(R.layout.activity_main);
    }

    public void saveSharedPreferences(String email, String password){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("loginEmail",email);
        editor.putString("loginPassword",password);
        editor.apply();

    }

    public void loadSharedPreferences(){
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);

        // Values
        String email = preferences.getString("loginEmail",null);
        String password = preferences.getString("loginPassword",null);

        // Then you want to query preferences for values such as username and password like
        if((email != null) && (password != null))
        {

            Log.d("Preferences email",email);
            Log.d("Preferences pass",password);

            if(loginRequest(email,password)){
                Log.d("Preferences","auto logged in");
                //intent to logged in activity
            }

        }
    }

    public void loginClick(View view) {
        EditText emailView = (EditText) findViewById(R.id.email);
        EditText passView = (EditText) findViewById(R.id.password);

        final String email = emailView.getText().toString();
        final String pass = passView.getText().toString();
        if(loginRequest(email,pass)) {
            saveSharedPreferences(email, pass);
            //intent to logged in activity
        }
    }

    public boolean loginRequest(String e,String p) {
        final String email=e;
        final String pass=p;
        final ValContainer<Boolean> loginResult = new ValContainer<Boolean>(false);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.6:80/onTimeServer/v1/login";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                Log.d("LOGIN",Response.toString());
                try {
                    if (!Response.getBoolean("error")){
                        loginResult.setVal(true);
                    }

                    else
                    {
                        TextView errorview =(TextView) findViewById(R.id.error);
                        errorview.setText(Response.getString("message"));
                        errorview.setVisibility(View.VISIBLE);
                        loginResult.setVal(false);
                    }
                }
                catch(JSONException e){
                    Log.d("JSONException","Try Login Again");
                    loginResult.setVal(false);
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
            public byte[] getBody()
            {
                try
                {
                    final String body = "&email=" + email +  // assumes username is final and is url encoded.
                            "&password=" + pass;            // assumes password is final and is url encoded.
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
        return loginResult.getVal();
    }

    public void registerClick(View view){
        Intent registerIntent = new Intent(loginActivity.this,registerActivity.class);
        startActivity(registerIntent);
    }
}

