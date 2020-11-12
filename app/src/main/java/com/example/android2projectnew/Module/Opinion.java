package com.example.android2projectnew.Module;

import android.widget.RatingBar;

import java.util.Date;

public class Opinion extends Filter {

    private String headline, companyName, id, userName;
    private String year1, year2, onDuty, pros, cons;
   // private double latitude, longitude;

    private RatingBar ratingBar;



    public Opinion() {
    }


    public Opinion(String headline, String companyName, String year1, String year2, String onDuty, String pros, String cons, String id, String userName, double latitude, double longitude, Date timeStamp) {
        super(latitude, longitude,timeStamp);
        this.headline = headline;
        this.companyName = companyName;
        this.year1 = year1;
        this.year2 = year2;
        this.onDuty = onDuty;
        this.pros = pros;
        this.cons = cons;
        this.id = id;
        this.userName = userName;

        //this.ratingBar = ratingBar;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



    /* public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getYear2() {
        return year2;
    }

    public void setYear2(String year2) {
        this.year2 = year2;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public String getPros() {
        return pros;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }
}
