package com.example.android2projectnew.Module;

import java.util.Date;

public class NotificationCommentLike {

    String pId, pUid, notification, sUid, sName, sImage;
    Date timeStamp;

    public NotificationCommentLike() {
    }

    public NotificationCommentLike(String pId, Date timeStamp, String pUid, String notification, String sUid, String sName, String sImage) {
        this.pId = pId;
        this.timeStamp = timeStamp;
        this.pUid = pUid;
        this.notification = notification;
        this.sUid = sUid;
        this.sName = sName;
        this.sImage = sImage;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }
}
