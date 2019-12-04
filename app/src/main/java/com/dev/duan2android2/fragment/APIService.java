package com.dev.duan2android2.fragment;

import com.dev.duan2android2.notification.MyResponse;
import com.dev.duan2android2.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:Application/json",
                    "Authorization:key=AAAA0ox2ou0:APA91bGJoFzw0oswdw1svyH6scwcUHIT7RRQRwP5hpqTR3W4iFvPNHE1PlU2zA5kVQ3Dt5L1BDslnVF_YmmeTDNE7o9BYH1Jl5mfWNNniPCQyrD5uYDn8S2yrZmVL0-RKFg4KR-jXS3X"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
