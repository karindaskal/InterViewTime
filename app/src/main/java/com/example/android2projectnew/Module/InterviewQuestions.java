package com.example.android2projectnew.Module;

import android.widget.PopupMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InterviewQuestions extends Filter implements Serializable {
    private String company;
    private String date;
    private  String job;
    private  String proses;
    private String question;
    private  String answer;
    private  String userName;
    private String id;
    private int likes;
    private int idQ;
    private String pComment;
    private List<Comment> commentList;


    public InterviewQuestions() {
    }

    public InterviewQuestions(String company, String date, String job, String proses, String question, String answer, String userName, String id, double latitude, double longitude, Date timeStamp, int likes,int idQ) {
        super(latitude, longitude,timeStamp);
        this.company = company;
        this.date = date;
        this.job = job;
        this.proses = proses;
        this.question = question;
        this.answer = answer;
        this.userName = userName;
        this.id = id;
        this.likes = 0;
        this.idQ = idQ;
        pComment = "0";
        commentList = new ArrayList<>();
    }

    public String getpComment() {
        return pComment;
    }

    public void setpComment(String pComment) {
        this.pComment = pComment;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getIdQ() {
        return idQ;
    }

    public void setIdQ(int idQ) {
        this.idQ = idQ;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getCompany() {
        return company;
    }

    public String getDate() {
        return date;
    }

    public String getJob() {
        return job;
    }

    public String getProses() {
        return proses;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setProses(String proses) {
        this.proses = proses;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
