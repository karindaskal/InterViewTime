package com.example.android2projectnew.Module;

public class UserProfileData {

    String job;
    String company;
    String userName;
    String id;
    String imageURL;
    String status;
    String search;
    NotificationCommentLike notificationCommentLike;


    public UserProfileData() {
    }

    public UserProfileData(String userName) {
        this.userName = userName;
        this.company = "";
        this.job = "";
    }

    public NotificationCommentLike getNotificationCommentLike() {
        return notificationCommentLike;
    }

    public void setNotificationCommentLike(NotificationCommentLike notificationCommentLike) {
        this.notificationCommentLike = notificationCommentLike;
    }

    public UserProfileData(String job, String company, String userName, String id, String imageURL, String status, String search) {
        this.job = job;
        this.company = company;
        this.userName = userName;
        this.id = id;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public UserProfileData(String userName, String id, String imageURL) {
        this.userName = userName;
        this.id = id;
        this.imageURL=imageURL;
    }
/*

    public UserProfileData(String job, String company, String userName) {
        this.job = job;
        this.company = company;
        this.userName = userName;
    }
*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJob() {
        return job;
    }

    public String getCompany() {
        return company;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
