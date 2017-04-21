package com.example.holykael.ontime_travellers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A TicketManager fragment containing a simple view.
 */
public class TicketManagerFragment extends android.support.v4.app.Fragment {

    public TicketManagerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TicketManagerFragment newInstance() {
        TicketManagerFragment fragment = new TicketManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ticket_manager_layout, container, false);
        return rootView;
    }
}