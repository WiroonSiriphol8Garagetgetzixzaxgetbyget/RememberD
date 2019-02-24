package com.example.simple.myrememberdfirebase;

public class MyQueryConnect {

    private int id_rmmb;
    private String activity;
    private String place;
    private String token;
    private String year;
    private String month;
    private String day;
    private String time;

    public MyQueryConnect(int id_rmmb, String activity, String place, String token, String year, String month, String day, String time) {
        this.id_rmmb = id_rmmb;
        this.activity = activity;
        this.place = place;
        this.token = token;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }

    public int getId_rmmb() {
        return id_rmmb;
    }

    public String getActivity() {
        return activity;
    }

    public String getPlace() {
        return place;
    }

    public String getToken() {
        return token;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }
}//MyQueryConnect
