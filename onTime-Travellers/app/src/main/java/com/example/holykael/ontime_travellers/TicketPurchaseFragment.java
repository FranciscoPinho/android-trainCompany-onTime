package com.example.holykael.ontime_travellers;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Holykael on 4/21/2017.
 */

public class TicketPurchaseFragment extends android.support.v4.app.Fragment {
    public TicketPurchaseFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TicketPurchaseFragment newInstance() {
        TicketPurchaseFragment fragment = new TicketPurchaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.purchase_ticket_layout, container, false);
        return rootView;
    }
}
