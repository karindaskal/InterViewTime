package com.example.android2projectnew.Module;

import java.io.Serializable;
import java.util.Date;

public class Salary extends Filter implements Serializable {
    private String fromYear;
    private String toYear;
    private String job;
    private String companySalary;
    private String jobStructure;
    private String grossSalary;
    private String jobSeniority;
    private String userName;
    private String id;


    public Salary() {
    }

    public Salary(String fromYear, String toYear, String job, String companySalary, String jobStructure, String grossSalary, String jobSeniority, String userName, String id, double latitude, double longitude, Date timeStamp) {
        super(latitude, longitude,timeStamp);
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.job = job;
        this.companySalary=companySalary;
        this.jobStructure = jobStructure;
        this.grossSalary = grossSalary;
        this.jobSeniority = jobSeniority;
        this.userName = userName;
        this.id = id;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompanySalary() {
        return companySalary;
    }

    public void setCompanySalary(String companySalary) {
        this.companySalary = companySalary;
    }

    public String getJobStructure() {
        return jobStructure;
    }

    public void setJobStructure(String jobStructure) {
        this.jobStructure = jobStructure;
    }

    public String getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(String grossSalary) {
        this.grossSalary = grossSalary;
    }

    public String getJobSeniority() {
        return jobSeniority;
    }

    public void setJobSeniority(String jobSeniority) {
        this.jobSeniority = jobSeniority;
    }
}
