package com.example.android2projectnew.Module;

import java.util.Date;

public class Filter {

    double latitude, Longitude;
    Date timeStamp;

    public Filter(double latitude, double longitude, Date timeStamp) {
        this.latitude = latitude;
        this.Longitude = longitude;
        this.timeStamp = timeStamp;
    }

    public Filter() {
    }


    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
