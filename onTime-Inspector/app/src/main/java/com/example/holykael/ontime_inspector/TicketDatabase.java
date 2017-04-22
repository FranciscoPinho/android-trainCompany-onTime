package com.example.holykael.ontime_inspector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class TicketDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="tickets.db";
    private static final int SCHEMA_VERSION=1;

    public TicketDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tickets (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT UNIQUE,userID INTEGER, trainDesignation TEXT, validation TINYINT, origin TEXT, destination TEXT, departureTime TEXT, arrivalTime TEXT, price DOUBLE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no-op, since will not be called until we need to define a 2nd schema version
    }

    public long insert(Ticket t) {
        ContentValues cv=new ContentValues();
        cv.put("uuid", t.getUuid());
        cv.put("userID", t.getUserID());
        cv.put("trainDesignation", t.getTrain());
        cv.put("validation", t.getValidation());
        cv.put("origin", t.getOrigin());
        cv.put("destination", t.getDestination());
        cv.put("departureTime", t.getDepartureTime());
        cv.put("arrivalTime", t.getArrivalTime());
        cv.put("price", t.getPrice());
        return getWritableDatabase().insert("tickets", null, cv);
    }
    public void update(String id, int validation) {
        ContentValues cv=new ContentValues();
        String[] args={id};
        cv.put("validation", validation);
        getWritableDatabase().update("tickets", cv, "uuid=?", args);
    }

    public void clearDatabase(){
        getWritableDatabase().execSQL("delete from tickets");
        getWritableDatabase().execSQL("vacuum");
    }

    public Cursor getAll() {
        return(getReadableDatabase()
                .rawQuery("SELECT _id,uuid,trainDesignation,validation,origin,destination,departureTime,arrivalTime,price FROM tickets ORDER BY trainDesignation",
                        null));
    }
    public Cursor getById(String uuid) {
        String[] args={uuid};
        return(getReadableDatabase()
                .rawQuery("SELECT _id,uuid,trainDesignation,validation,origin,destination,departureTime,arrivalTime,price FROM tickets WHERE uuid=?",
                        args));
    }
    public String getUuid(Cursor c){return(c.getString(1)); }
    public String getTrain(Cursor c) {
        return(c.getString(2));
    }
    public int getValidation(Cursor c) {
        return(c.getInt(3));
    }
    public String getOrigin(Cursor c) {
        return(c.getString(4));
    }
    public String getDestination(Cursor c) {
        return(c.getString(5));
    }
    public String getDepartureTime(Cursor c) {
        return(c.getString(6));
    }
    public String getArrivalTime(Cursor c) {
        return(c.getString(7));
    }
    public Double getPrice(Cursor c) {
        return(c.getDouble(8));
    }

}
