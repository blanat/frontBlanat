package com.example.myapplication.retrofit;

import com.example.myapplication.model.Deal;
import com.example.myapplication.model.listData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DealApi {
    // Define your API endpoints here
    @Multipart
    @POST("/api/deals/create")
    Call<RequestBody> uploadDeal(
            @Part("deal") RequestBody deal,
            @Part List<MultipartBody.Part> images
    );


    @GET("/api/deals/listDeals")
    Call<List<listData>> getListDeals();

}

