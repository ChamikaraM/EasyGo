package com.example.chamiya.egot.models;

/**
 * Created by Chamiya on 2/25/2018.
 */

public class BusSchedule {
    private String bus_schedule_id,routeNo,from,to;

    public BusSchedule(){

    }

    public BusSchedule(String bus_schedule_id,String routeNo, String from, String to){
        this.bus_schedule_id = bus_schedule_id;
        this.routeNo = routeNo;
        this.from = from;
        this.to = to;
        //this.status = status;


    }

    public String getBus_schedule_id() {
        return bus_schedule_id;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
