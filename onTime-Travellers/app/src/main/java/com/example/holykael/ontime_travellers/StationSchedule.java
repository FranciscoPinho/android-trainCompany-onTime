package com.example.holykael.ontime_travellers;



/**
 * Created by Holykael on 4/20/2017.
 */

public class StationSchedule {
    int id;
    String trainDesignation;
    String origin;
    String destination;
    Double distance;
    String departureTime;
    String arrivalTime;

    public StationSchedule(int i,String t,String src,String dest,Double di,String depar,String arriv){
        id=i;
        trainDesignation=t;
        origin=src;
        destination=dest;
        distance=di;
        departureTime=depar;
        arrivalTime=arriv;
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

}
