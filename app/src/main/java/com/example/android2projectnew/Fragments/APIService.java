package com.example.android2projectnew.Fragments;

import com.example.android2projectnew.Notifications.MyResponse;
import com.example.android2projectnew.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
        {
                "content-Type:application/json",
                "Authorization:key=AAAAwKfas6k:APA91bERR7ewfM62lfEujV-24Hyzq_OZIoBzvRAcPIYFhB7MKAOUE9s6LL1FK1QtC98xkkvosNeJUzqLqvyrR051Emofj8e9Cleua_Ttb0hcoy8xvXEIDnxXfxyiIfHvDZ82j-rnxDsu"
        }
    )

    @POST("fcm/send")

    Call<MyResponse> sendNotification(@Body Sender body);
    Call<MyResponse> sendOreoNotification(@Body Sender body);
}
