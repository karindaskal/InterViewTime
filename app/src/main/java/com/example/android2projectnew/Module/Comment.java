package com.example.android2projectnew.Module;

import com.example.android2projectnew.Notifications.Data;

import java.util.Date;

public class Comment {
    String old, comment, uId, uEmail,uDp, uName;
    Date timpeStamp;

    public Comment() {
    }

    public Comment(String old, String comment, Date timpeStamp, String uId, String uEmail, String uDp, String uName) {
        this.old = old;
        this.comment = comment;
        this.timpeStamp = timpeStamp;
        this.uId = uId;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimpeStamp() {
        return timpeStamp;
    }

    public void setTimpeStamp(Date timpeStamp) {
        this.timpeStamp = timpeStamp;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
