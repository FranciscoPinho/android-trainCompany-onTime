package com.example.holykael.ontime_travellers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class onTimeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ArrayList<StationSchedule> schedules;

    protected double distance=0;
    protected LocalTime departure;
    protected LocalTime arrival;

    QRCodeGenerator mService;
    boolean mBound = false;


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to QRCodeService, cast the IBinder and get QRCodeService instance
            QRCodeGenerator.QRCodeBinder binder = (QRCodeGenerator.QRCodeBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        schedules= new ArrayList<>();
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_ontime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),'L');

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleFragment fragment = (ScheduleFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);

                if(fragment.getDirection()=='P')
                    fragment.setLayoutDir('L');
                else fragment.setLayoutDir('P');;
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            @Override
            public void onPageSelected(int position) {

                switch(position){
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fab.setVisibility(View.INVISIBLE);
                        final Spinner origin = (Spinner) findViewById(R.id.originspinner);
                        final Spinner destination = (Spinner) findViewById(R.id.destspinner);
                        final Spinner trains = (Spinner) findViewById(R.id.trainspinner);
                        destination.setSelection(5);
                        updateTrainSpinner(trains,origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                updateTrainSpinner(trains,origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                                updateValues(trains.getSelectedItem().toString(),origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                                updateTextFields();
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                updateTrainSpinner(trains,origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                                updateValues(trains.getSelectedItem().toString(),origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                                updateTextFields();
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        trains.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                updateValues(trains.getSelectedItem().toString(),origin.getSelectedItem().toString(),destination.getSelectedItem().toString());
                                updateTextFields();
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        break;
                    case 2:
                        updateFileList();
                        final Spinner filespinner = (Spinner) findViewById(R.id.filespinner);
                        fab.setVisibility(View.INVISIBLE);
                        filespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                String filename = filespinner.getSelectedItem().toString();
                                File file = new File(getCacheDir()+"/Tickets/",filename);
                                ImageView qrCode = (ImageView) findViewById(R.id.QRCode);
                                Bitmap bm = BitmapFactory.decodeFile(file.toString());
                                qrCode.setImageBitmap(bm);
                                getTicketInfo(filename.charAt(0));
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        break;
                    default:
                        fab.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
        requestSchedules(new VolleyCallback(){
            @Override
            public void onSuccess(){
            }
            @Override
            public void onSuccess(JSONObject response){
                try {
                    for (int i = 0; i < response.names().length(); i++) {
                        if(!response.names().getString(i).equals("error") && !response.names().getString(i).equals("message")){
                            StationSchedule ss = new StationSchedule((JSONObject) response.get(response.names().getString(i)));
                            schedules.add(ss);
                        }
                    }
                }
                catch(JSONException e){
                    Log.d("PARSING",e.getMessage());
                }
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, QRCodeGenerator.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ontime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestSchedules(final VolleyCallback callback){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/schedules";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>() {
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

    public int getStationID(String station){
        switch(station){
            case "Sao Bento":
                return 0;
            case "Gaia":
                return 1;
            case "Aveiro":
                return 2;
            case "Coimbra":
                return 3;
            case "Oriente":
                return 4;
            case "Santa Apolonia":
                return 5;
            default:
                return -1;
        }
    }
    public void updateTrainSpinner(Spinner trains,String origin, String dest){
        int nr =getStationID(dest)-getStationID(origin);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this, R.array.trainsL, R.layout.custom_dropdown_list);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this, R.array.trainsP, R.layout.custom_dropdown_list);
        if(nr>0){
            trains.setAdapter(adapter1);
        }
        else if(nr<0){
            trains.setAdapter(adapter2);
        }

    }
    public void updateValues(String train,String origin,String destination){
        distance=0;
        arrival=LocalTime.parse("00:00");
        departure=LocalTime.parse("00:00");

        boolean start = false;
        if(origin==destination){
            return;
        }
        for(int i=0;i<schedules.size();i++){
            if(schedules.get(i).getOrigin().equals(origin) && schedules.get(i).getTrainDesignation().equals(train)){
                departure=schedules.get(i).getDepartureTime();
                start=true;
            }
            if(start){
                distance+=schedules.get(i).getDistance();
                if(schedules.get(i).getDestination().equals(destination)){
                    arrival=schedules.get(i).getArrivalTime();
                    return;
                }
            }
        }
    }
    public void updateTextFields(){
        TextView dep = (TextView) findViewById(R.id.departure);
        TextView arr = (TextView) findViewById(R.id.arrival);
        TextView dis = (TextView) findViewById(R.id.distance);
        TextView pri = (TextView) findViewById(R.id.price);
        DecimalFormat df = new DecimalFormat("#.00");

        dep.setText(departure.toString("HH:mm"));
        arr.setText(arrival.toString("HH:mm"));
        dis.setText(df.format(distance)+" Km");
        pri.setText(df.format(calcPrice())+" €");
    }

    public double calcPrice(){
        return 1.50+distance/15;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        char direction;
        public SectionsPagerAdapter(FragmentManager fm,char dir) {
            super(fm);
            direction=dir;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(direction=='P')
                    return ScheduleFragment.newInstance('P');
                    else return ScheduleFragment.newInstance('L');
                case 1:
                    return TicketPurchaseFragment.newInstance();
                case 2:
                    return TicketManagerFragment.newInstance();
                default:
                    return null;
            }
        }
        // Force a refresh of the page when a different fragment is displayed
        @Override
        public int getItemPosition(Object object) {
            // this method will be called for every fragment in the ViewPager
            if (object instanceof TicketManagerFragment || object instanceof TicketPurchaseFragment) {
                return POSITION_UNCHANGED; // don't force a reload
            } else {
                // POSITION_NONE means something like: this fragment is no longer valid
                // triggering the ViewPager to re-build the instance of this fragment.
                return POSITION_NONE;

        }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Schedules";
                case 1:
                    return "Purchase";
                case 2:
                    return "Ticket Manager";
            }
            return null;
        }


    }

    public void buyTicket(View view){
       final ValContainer<Boolean> confirm = new ValContainer<>(false);
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Purchase")
                    .setMessage(
                            "Are you sure you want to purchase the ticket?"
                    )
                    .setPositiveButton(
                            "Purchase",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ticketPurchaseRequest(new VolleyCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            String payload="";
                                            try {
                                                for (int i = 0; i < response.names().length(); i++) {
                                                    if(!response.names().getString(i).equals("error") && !response.names().getString(i).equals("message")){
                                                        payload += response.names().getString(i) +"="+response.get(response.names().getString(i))+";";
                                                    }
                                                }

                                                mService.generateQRCode(payload);
                                                updateFileList();

                                            }
                                            catch(JSONException e){
                                                Log.d("PARSING",e.getMessage());
                                            }
                                        }
                                    });
                                }
                            })
                    .setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    confirm.setVal(false);
                                }
                            }).show();


    }

    public void ticketPurchaseRequest(final VolleyCallback callback){
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getSharedPreferences("Data", Context.MODE_PRIVATE);
        // Values
        final String email = preferences.getString("loginEmail",null);
        Spinner o = (Spinner) findViewById(R.id.originspinner);
        Spinner d = (Spinner) findViewById(R.id.destspinner);
        Spinner t = (Spinner) findViewById(R.id.trainspinner);
        final String trainDesignation=t.getSelectedItem().toString();
        final int validation = 0;
        final String origin = o.getSelectedItem().toString();
        final String destination = d.getSelectedItem().toString();
        final String departureTime=departure.toString("HH:mm");
        final String arrivalTime=arrival.toString("HH:mm");
        final Double price = calcPrice();
        final DecimalFormat df = new DecimalFormat("#.00");
        //request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://"+getString(R.string.server)+"/onTimeServer/v1/ticket";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Response) {
                try {
                    if (Response.getInt("error")==0){
                        callback.onSuccess(Response);
                    }

                    else
                    {
                        TextView errorview =(TextView) findViewById(R.id.error);
                        errorview.setText(Response.getString("message"));
                        errorview.setVisibility(View.VISIBLE);
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
                            "&trainDesignation=" + trainDesignation +
                            "&validation=" + validation +
                            "&origin=" + origin +
                            "&destination=" + destination +
                            "&departureTime=" + departureTime +
                            "&arrivalTime=" + arrivalTime +
                            "&price=" + df.format(price);

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

    public void updateFileList(){
        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                final Bitmap bitmap;
                    File dir = new File(getCacheDir(), "Tickets");
                    final List<String> list = new ArrayList<String>();
                    if (dir.exists()) {
                       for (File f : dir.listFiles()) {
                           list.add(f.getName());
                        }
                    }
                    runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                        @Override
                        public void run() {
                            final Spinner filespinner = (Spinner) findViewById(R.id.filespinner);
                            ArrayAdapter<String> adp1 = new ArrayAdapter<String>(get_this(),
                                    android.R.layout.simple_list_item_1, list);
                            filespinner.setAdapter(adp1);
                            return;
                        }
                    });
                    return;
                }
        });
        t.start();

    }
    public Activity get_this(){
        return this;
    }
    public void getTicketInfo(char beg){
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        String ticketinfo=preferences.getString("Ticket"+beg,null);
        if(ticketinfo!=null){
            TextView infoview = (TextView) findViewById(R.id.ticketinfo);
            infoview.setText(ticketinfo);
        }
    }

}
