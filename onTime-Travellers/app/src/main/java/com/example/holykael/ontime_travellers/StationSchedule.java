package com.example.holykael.ontime_travellers;

import android.util.Log;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Holykael on 4/20/2017.
 */

public class StationSchedule {
    int id;
    String trainDesignation;
    String origin;
    String destination;
    Double distance;
    LocalTime departureTime;
    LocalTime arrivalTime;


    public StationSchedule(int i,String t,String src,String dest,Double di,LocalTime depar,LocalTime arriv){
        id=i;
        trainDesignation=t;
        origin=src;
        destination=dest;
        distance=di;
        departureTime=depar;
        arrivalTime=arriv;
    }

    public StationSchedule(JSONObject obj){
        try{
            id=obj.getInt("id");
            trainDesignation=obj.getString("train");
            origin=obj.getString("origin");
            destination=obj.getString("destination");
            distance=obj.getDouble("distance");
            departureTime=LocalTime.parse(obj.getString("departureTime"));
            arrivalTime=LocalTime.parse(obj.getString("arrivalTime"));
        }
        catch(JSONException e){
            Log.d("FAILED PARSING",e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainDesignation() {
        return trainDesignation;
    }

    public void setTrainDesignation(String trainDesignation) {
        this.trainDesignation = trainDesignation;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

}
