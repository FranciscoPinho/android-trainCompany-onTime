package com.example.holykael.ontime_inspector;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Holykael on 4/21/2017.
 */

public class Ticket {
    String uuid;
    String userID;
    String train;
    int validation;
    String origin;
    String destination;
    String departureTime;
    String arrivalTime;
    double price;

    public Ticket(String uid, String user, String t, int val, String o, String d, String dep,String arr,Double pric){
        uuid=uid;
        userID=user;
        train=t;
        validation=val;
        origin=o;
        destination=d;
        departureTime=dep;
        arrivalTime=arr;
        price=pric;
    }
    public Ticket(JSONObject obj){
        try{
            uuid=obj.getString("id");
            userID=obj.getString("userID");
            train=obj.getString("train");
            origin=obj.getString("origin");
            destination=obj.getString("destination");
            departureTime=obj.getString("departureTime");
            arrivalTime=obj.getString("arrivalTime");
            price=obj.getDouble("distance");
        }
        catch(JSONException e){
            Log.d("FAILED PARSING",e.getMessage());
        }
    }
    public String getUuid(){
        return uuid;
    }
    public void setUuid(String u){
        uuid=u;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public int getValidation() {
        return validation;
    }

    public void setValidation(int validation) {
        this.validation = validation;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
