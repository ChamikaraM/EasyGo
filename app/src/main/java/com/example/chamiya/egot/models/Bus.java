package com.example.chamiya.egot.models;

/**
 * Created by Chamiya on 2/25/2018.
 */

public class Bus {
    private String bus_id,registeredNo,from,to,routeNo,email;

    public Bus(){

    }

    public Bus(String bus_id, String registeredNo, String from, String to, String routeNo, String email){
        this.bus_id = bus_id;
        this.registeredNo = registeredNo;
        this.from = from;
        this.to = to;
        //this.status = status;
        this.routeNo = routeNo;
        this.email = email;
    }

    public String getBus_id() {
        return bus_id;
    }

    public String getRegisteredNo() {
        return registeredNo;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public String getEmail() {
        return email;
    }



    public void setRegisteredNo(String registeredNo) {
        this.registeredNo = registeredNo;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
