package com.example.holykael.ontime_travellers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;


public class registerActivity extends AppCompatActivity {
    ArrayList<String> listOfPattern=new ArrayList<>();
    boolean validateNumber=false;
    boolean validateEmail=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ptVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{14}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{13}$";
        listOfPattern.add(ptAmeExp);
        setContentView(R.layout.activity_register);
        EditText cardnr = (EditText) findViewById(R.id.card_nr);
        cardnr.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ccNum = s.toString();
                for(String p:listOfPattern){
                    if(ccNum.matches(p)){
                        validateNumber=true;
                        break;
                    }
                    else validateNumber=false;
                }
            }
        });

        EditText email = (EditText) findViewById(R.id.email) ;
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String e = s.toString();
                if(isEmailValid(e)){
                    validateEmail=true;
                }
                else validateEmail=false;
            }
        });

    }

    public boolean validatePassword(){
        EditText password =(EditText) findViewById(R.id.password);
        EditText confirm =(EditText) findViewById(R.id.passwordconfirm);
        if(password.getText().toString().isEmpty() || confirm.getText().toString().isEmpty())
            return false;
        if(password.getText().toString().equals(confirm.getText().toString()))
            return true;
        else {
            password.setError(getString(R.string.password_err));
            return false;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validateAllInputs(){
        EditText email =(EditText) findViewById(R.id.email);
        EditText cardnr = (EditText) findViewById(R.id.card_nr);
        EditText username = (EditText) findViewById(R.id.username);
        EditText cardname = (EditText) findViewById(R.id.card_name);
        boolean allvalidated=true;

        if(!validatePassword())
            allvalidated=false;
        if(!validateEmail){
            email.setError(getString(R.string.email_err));
            allvalidated=false;
        }
        if(!validateNumber) {
            cardnr.setError(getString(R.string.creditnr_err));
            allvalidated=false;
        }
        if(username.getText().toString().isEmpty())
            allvalidated=false;
        if(cardname.getText().toString().isEmpty())
            allvalidated=false;
        return allvalidated;
    }

    public void register(View view){
        if(validateAllInputs())
            registerRequest(new VolleyCallback(){
                @Override
                public void onSuccess(){
                    EditText email =(EditText) findViewById(R.id.email);
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("loginEmail", email.getText().toString());
                    editor.commit();
                    finish();
                }
                @Override
                public void onSuccess(JSONObject r){
                }
            });

    }

    public void registerRequest(final VolleyCallback callback){

        EditText username = (EditText) findViewById(R.id.username);
        EditText e =(EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText name = (EditText) findViewById(R.id.card_name);
        EditText nr = (EditText) findViewById(R.id.card_nr);
        final String user = username.getText().toString();
        final String email = e.getText().toString();
        final String pass = password.getText().toString();
        final String cardname = name.getText().toString();
        final String number = nr.getText().toString();

        SharedPreferences preferences = this.getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String uuid = preferences.getString("loginUUID",null);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/register";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
                public void onResponse(JSONObject Response) {
                    Log.d("REGISTER",Response.toString());
                    try {
                        switch(Response.getInt("error")){
                            case 0:
                                callback.onSuccess();
                                break;
                            default:
                                TextView errorview =(TextView) findViewById(R.id.error);
                                errorview.setText(Response.getString("message"));
                                errorview.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    catch(JSONException e){
                        Log.d("JSONException","Try Register Again");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    e.printStackTrace();
                    TextView errorview = (TextView) findViewById(R.id.error);
                    if(e.networkResponse!=null)
                        if( e.networkResponse.statusCode==400) {
                            errorview.setText(getString(R.string.error_400_register));
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
                        final String body =
                                "&name=" + user + "&email=" + email + "&password=" + pass +
                                "&creditname=" + cardname + "&creditnumber=" + number + "&uuid=" + uuid;
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
}
