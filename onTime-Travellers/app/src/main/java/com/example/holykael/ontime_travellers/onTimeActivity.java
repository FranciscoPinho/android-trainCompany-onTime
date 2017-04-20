package com.example.holykael.ontime_travellers;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class onTimeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    Log.d("JSONException","Couldn't get schedules");
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ontime, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ScheduleFragment extends Fragment {
        private char layoutDir = 'L';
        public ScheduleFragment() {
        }
        public void setLayoutDir(char dir){
            layoutDir=dir;
        }
        public char getDirection(){
            return layoutDir;
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ScheduleFragment newInstance(char layoutDir) {
            ScheduleFragment fragment = new ScheduleFragment();
            fragment.setLayoutDir(layoutDir);
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if(this.layoutDir=='P')
             rootView = inflater.inflate(R.layout.schedules_direction_porto, container, false);
            else  rootView = inflater.inflate(R.layout.schedules_direction_lisboa, container, false);
            return rootView;
        }
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
                    return PlaceholderFragment.newInstance(position+1);
                case 2:
                    return PlaceholderFragment.newInstance(position+1);
                default:
                    return PlaceholderFragment.newInstance(position+1);
            }
        }
        // Force a refresh of the page when a different fragment is displayed
        @Override
        public int getItemPosition(Object object) {
            // this method will be called for every fragment in the ViewPager
            if (object instanceof PlaceholderFragment) {
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
}
