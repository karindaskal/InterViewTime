package com.example.android2projectnew.Module;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.load.engine.Resource;
import com.example.android2projectnew.R;

public enum Tabs {

    /*    OPINION("Opinion1","Opinion"),
        INTERVIEWQUESTIONS("Questions","Questions"),
        SALARY("salary","salary");*/

    OPINION(R.string.opinion, "Opinion"),
    INTERVIEWQUESTIONS(R.string.question, "Questions"),
    SALARY(R.string.salary_num, "salary");

    private String name;
    private int nameint;

    private int mResourceId;

    private String nameForDataBase;

    // String string = get().getString(android.R.string.cancel);

    private Tabs(int id) {
        mResourceId = id;
    }

/*    Tabs(String name) {
        this.name = name;
    }*/

    Tabs(int nameint, String nameForDataBase) {
        this.nameint = nameint;
        this.nameForDataBase = nameForDataBase;
    }


    public String getNameForDataBase() {
        return nameForDataBase;
    }

    public int getNameint() {
        return nameint;
    }

    public void setNameint(int nameint) {
        this.nameint = nameint;
    }

    public int getmResourceId() {
        return mResourceId;
    }

    public void setmResourceId(int mResourceId) {
        this.mResourceId = mResourceId;
    }

    public void setNameForDataBase(String nameForDataBase) {
        this.nameForDataBase = nameForDataBase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull

/*
    public String toString() {

    */
/*    switch(ref){
            case OPINION:
                return res.getString(R.string.opinion);

            case INTERVIEWQUESTIONS:
                return res.getString(R.string.question);

            case SALARY:
                return res.getString(R.string.salary_num);

        }
        return res.getString(mResourceId);*//*

   getClass().get
    }
*/

    public String toString(Tabs ref, Resources c){
        switch(ref){
            case OPINION:
                return c.getString(R.string.opinion);

            case INTERVIEWQUESTIONS:
                return c.getString(R.string.question);

            case SALARY:
                return c.getString(R.string.salary_num);

        }
        return c.getString(mResourceId);
    }



/*
    public String toString() {
  *//*      switch(ref){
            case OPINION:
                return c.getString(R.string.opinion);

            case INTERVIEWQUESTIONS:
                return c.getString(R.string.question);

            case SALARY:
                return c.getString(R.string.salary_num);

        }
        return c.getString(R.string.opinion);*//*
        //}
        return AppCompatActivity. ().getString(mResourceId);

    }

    */


}
