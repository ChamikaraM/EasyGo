package com.example.chamiya.egot.models;

/**
 * Created by Chamiya on 2/25/2018.
 */

public class Tracking {
    private String email,uid,lat,lng;

    public Tracking() {
    }



    public Tracking(String email, String uid, String lat, String lng)  {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }

    public String getEmail() {
        return email;
    }

    public String getLat() {
        return lat;
    }

    public String getUid() {
        return uid;
    }

    public String getLng() {
        return lng;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
