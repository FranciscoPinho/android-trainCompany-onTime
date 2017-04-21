package com.example.holykael.ontime_inspector;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TicketList extends ListActivity {

    private TicketDatabase helper;
    Cursor model=null;
    TicketAdapter adapter=null;



    //Restaurant adapter with custom view
    class TicketAdapter extends CursorAdapter {
        TicketAdapter(Cursor c) {
            super(TicketList.this, c);
        }


        public void bindView(View row, Context ctxt, Cursor c) {
            ((TextView)row.findViewById(R.id.uuid)).setText(helper.getUuid(c));
            ((TextView)row.findViewById(R.id.train)).setText("Train: "+helper.getTrain(c));
            ((TextView)row.findViewById(R.id.validation)).setText("Validated: "+helper.getValidation(c));
            ((TextView)row.findViewById(R.id.origin)).setText("Origin: "+helper.getOrigin(c));
            ((TextView)row.findViewById(R.id.destination)).setText("Destination: "+helper.getDestination(c));
            ((TextView)row.findViewById(R.id.price)).setText("Price: "+helper.getPrice(c)+"€");
        }
        public View newView(Context ctxt, Cursor c, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            return(row);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ticket_list);
        helper = new TicketDatabase(this);
        model=helper.getAll();
        startManagingCursor(model);
        adapter=new TicketAdapter(model);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
      finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        helper.close();
    }
}
