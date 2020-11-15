package com.example.android2projectnew.Lists;

import com.example.android2projectnew.Module.InterviewQuestions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionsList {

    private List<InterviewQuestions> questions = new ArrayList<>();


    private QuestionsList() {}
    static QuestionsList mySingleton;
    public static QuestionsList getMySingleton() {
        if (mySingleton == null) {

            mySingleton = new QuestionsList();
        }
        return mySingleton;
    }
    public void add (String company, String date, String job, String proses, String question, String answer, String userName, String id, double latitude, double longtitude, Date timeStamp, int likes,int idQ){
        questions.add(new InterviewQuestions(company,date,job,proses,question,answer,userName,id, latitude,longtitude,timeStamp, likes,idQ));
    }
    public List<InterviewQuestions> read(){

        return questions;
}


}

