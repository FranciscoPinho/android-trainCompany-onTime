package com.example.holykael.ontime_travellers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment to display schedules
 */
public class ScheduleFragment extends android.support.v4.app.Fragment {

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
            //fill schedules with server data - OPTIONAL IMPROVEMENT
        }
}

