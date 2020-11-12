package com.example.android2projectnew.Module;

import android.content.Context;
import android.content.res.Resources;

import com.example.android2projectnew.R;

import java.util.ArrayList;
import java.util.List;

public class JobList {
    private static String[] jobs = null;
    private JobList() {}
    static JobList mySingleton;
    public static JobList getMySingleton() {
        if (mySingleton == null) {

            mySingleton = new JobList();
        }
        return mySingleton;
    }
    public void add (String company, String date, String job, String proses, String question, String answer){
    }
    public String[] read(Context context){

        Resources res = context.getResources();
        jobs = res.getStringArray(R.array.jobList);
//        if(jobs==null)
//
//        {
//            jobs=new ArrayList<>();
//            jobs.add("בדיקות QA");
//            jobs.add("הוראה והדרכה") ;
//            jobs.add("מהנדס FPGA");
//            jobs.add("מתכנת");
//            jobs.add(context.getString(R.string.app_name));
//
//        }

        return jobs;
    }
}
