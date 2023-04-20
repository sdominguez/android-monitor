package com.example.monitor.api;

import com.squareup.moshi.Json;


public class Properties {
    @Json(name="mag")
    private double magnitude;
    private String place;
    private long time;

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }
}
