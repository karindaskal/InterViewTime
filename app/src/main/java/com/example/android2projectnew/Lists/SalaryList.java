package com.example.android2projectnew.Lists;

import com.example.android2projectnew.Module.Salary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalaryList {
    private List<Salary> salaries = new ArrayList<>();


    private SalaryList() {}

    static SalaryList mySingleton;
    public static SalaryList getMySingleton() {
        if (mySingleton == null) {

            mySingleton = new SalaryList();
        }
        return mySingleton;
    }

    public void add (String fromYear, String toYear, String job,String companySalary,  String jobStructure, String grossSalary, String jobSeniority,String userName,String id,double latitude, double longtitude, Date timeStamp){
        salaries.add(new Salary(fromYear,toYear,job,companySalary,jobStructure,grossSalary,jobSeniority,userName,id,latitude,longtitude, timeStamp ));
    }
    public List<Salary> read(){

        return salaries;
    }

}
