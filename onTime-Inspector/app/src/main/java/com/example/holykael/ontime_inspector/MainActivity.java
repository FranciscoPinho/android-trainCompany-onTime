package com.example.holykael.ontime_inspector;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    TextView message;
    String contents;
    String uid;
    int validation;
    String signature;
    String trainChoice = "L1";

    TicketDatabase tickets;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        setContentView(R.layout.activity_main);
        tickets = new TicketDatabase(this);
        message = (TextView) findViewById(R.id.message);
        final Spinner trains = (Spinner) findViewById(R.id.listtrains);
        trains.setSelection(0);
        trains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                trainChoice=trains.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        tickets.close();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("Message", message.getText());
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        message.setText(bundle.getCharSequence("Message"));
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if(validateTicket(contents)){
                    TextView result = (TextView) findViewById(R.id.validationresult);
                    result.setText("Successfully Validated");
                    findUpdateTicket(uid);
                }
                else{
                    TextView result = (TextView) findViewById(R.id.validationresult);
                    result.setText("No match");
                }
                message.setText("Format: " + format + "\nMessage: " + contents);
            }
        }
    }

    public boolean validateTicket(String contents){
        try {

            getSignature(contents);
            String hash = makeHash(uid);
            Log.d("HASH",hash);
            if(hash.equals("error")){
                TextView result = (TextView) findViewById(R.id.error);
                result.setVisibility(View.VISIBLE);
                result.setText("This ticket was already validated");
                return false;
            }
            PublicKey pub = getPublicKeyFromString("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANcSMei15vwZYZx+lnqzoFbaMayFdtzf\n" +
                    "KJPle1RblYt+r1Kt/XfQSxaAS1tr2cBFWLewws1ijb1oJ7YkPR2ybAECAwEAAQ==");
            Signature sg = Signature.getInstance("SHA1WithRSA");
             //sg.initVerify(pub);                                       // supply the public key
             //sg.update(hash.getBytes());                                // supply the data to verify
             //signature=signature + "==";
             //Log.d("SIG","im here");
             //return sg.verify(signature.getBytes(),0,88);                      // verify the signature (output) using the original dat
             return signature.equals(hash);
        }
        catch (Exception ex) {
            Log.d("Exception",ex.getMessage());
            return false;
        }
    }
    public void getSignature(String contents){
        StringTokenizer tokenizer = new StringTokenizer(contents,";");
        Log.d("D",""+tokenizer.countTokens());
        while(tokenizer.hasMoreTokens()) {
            String finals=tokenizer.nextToken();
            Log.d("FINALS",finals);
            tokenizeFinal(finals);
        }

    }
    public String makeHash(String uid){
        Cursor t=findTicket(uid);
        AeSimpleSHA1 sha = new AeSimpleSHA1();
        if(t!=null)
        try {
            if(tickets.getValidation(t)==1)
                return "error";
            String ret =sha.SHA1(tickets.getUuid(t));
            return ret;
        }
        catch(Exception e){
            Log.d("Ex",e.getMessage());
        }
        return "";
    }
    public Cursor findTicket(String id){
        return tickets.getById(id);
    }
    public void findUpdateTicket(String id){
        tickets.update(id,1);
    }
    public void tokenizeFinal(String string){

        StringTokenizer tokenizer = new StringTokenizer(string,"=");
        while(tokenizer.hasMoreTokens()) {
            switch (tokenizer.nextToken()){
                case "id":
                    uid=tokenizer.nextToken();
                    Log.d("TOKEN",uid);
                    break;
                case "validation":
                    validation=Integer.parseInt(tokenizer.nextToken());
                    Log.d("TOKEN",""+validation);
                    break;
                case "signature":
                    signature=tokenizer.nextToken();
                    Log.d("TOKEN",signature);
                    break;
            }
        }
    }
    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;
        byte[] encoded = Base64.decode(publicKeyPEM,Base64.DEFAULT);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    public void requestTickets(final VolleyCallback callback){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/tickets";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    switch(Response.getInt("error")){
                        case 0:
                            callback.onSuccess(Response);
                            break;
                        default:
                            TextView errorview =(TextView) findViewById(R.id.error);
                            errorview.setText(Response.getString("message"));
                            errorview.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                catch(JSONException e){
                    Log.d("JSONException",e.getMessage());
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
                        errorview.setText(getString(R.string.error_timeout));
                        errorview.setVisibility(View.VISIBLE);
                    }
            }
        }) {

            @Override
            public byte[] getBody(){
                try
                {
                    final String body = "&trainDesignation="+trainChoice;
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

    public void syncTicket(final VolleyCallback callback, final int val, final String id){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/sync";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    switch(Response.getInt("error")){
                        case 0:
                            callback.onSuccess(Response);
                            TextView errorview =(TextView) findViewById(R.id.error);
                            errorview.setText(Response.getString("message"));
                            errorview.setVisibility(View.VISIBLE);
                            break;
                        default:
                            errorview =(TextView) findViewById(R.id.error);
                            errorview.setText(Response.getString("message"));
                            errorview.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                catch(JSONException e){
                    Log.d("JSONException",e.getMessage());
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
                        errorview.setText(getString(R.string.error_timeout));
                        errorview.setVisibility(View.VISIBLE);
                    }
            }
        }) {

            @Override
            public byte[] getBody(){
                try
                {
                    final String body = "&uuid="+id+
                                        "&validation="+val;
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
    public void sync(View view){
        Cursor c = tickets.getAll();
        while (c.moveToNext()) {
            syncTicket(new VolleyCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onSuccess(JSONObject o) {

                }
            },tickets.getValidation(c),tickets.getUuid(c));
        }
    }
    public void downloadTickets(View view){
        requestTickets(new VolleyCallback(){
            @Override
            public void onSuccess(){
            }
            @Override
            public void onSuccess(JSONObject response){
                try {
                    for (int i = 0; i < response.names().length(); i++) {
                        if(!response.names().getString(i).equals("error") && !response.names().getString(i).equals("message")){
                            Ticket ss = new Ticket((JSONObject) response.get(response.names().getString(i)));
                            tickets.insert(ss);
                            Log.d("Ticket",ss.getUuid());
                        }
                    }
                }
                catch(JSONException e){
                    Log.d("PARSING",e.getMessage());
                }
            }
        });
    }

    public void listView(View view){
        Intent i=new Intent(MainActivity.this, TicketList.class);
        startActivity(i);
    }
    public void EmptyDB(View view){
        tickets.clearDatabase();
    }
    public class AeSimpleSHA1 {
        private String convertToHex(byte[] data) {
            StringBuilder buf = new StringBuilder();
            for (byte b : data) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        }

        public  String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] textBytes = text.getBytes("iso-8859-1");
            md.update(textBytes, 0, textBytes.length);
            byte[] sha1hash = md.digest();
            return convertToHex(sha1hash);
        }
    }
}
