package com.example.android2projectnew.Lists;

import com.example.android2projectnew.Module.Opinion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpinionList {
    private List<Opinion> opinionList = new ArrayList<>();

    private OpinionList() {}

    static OpinionList mySingleton;

    public static OpinionList getMySingleton() {
        if (mySingleton == null) {

            mySingleton = new OpinionList();
        }
        return mySingleton;
    }
    public void add (String headline, String companyName, String year1, String year2, String onDuty, String pros, String cons, String id, String userName, double latitude, double longtitude, Date timeStamp ){
        opinionList.add(new Opinion(headline,companyName,year1,year2,onDuty,pros,cons,id,userName,latitude,longtitude, timeStamp));
    }
    public List<Opinion> read(){

        return opinionList;
    }
}
