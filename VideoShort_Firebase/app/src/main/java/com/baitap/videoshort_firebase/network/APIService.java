package com.baitap.videoshort_firebase.network;

import com.baitap.videoshort_firebase.models.MessageVideo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("getvideos.php")
    Call<com.baitap.videoshort_firebase.models.MessageVideo> getVideos();
}
