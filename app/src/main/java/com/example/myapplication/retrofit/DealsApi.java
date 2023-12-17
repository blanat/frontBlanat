package com.example.myapplication.retrofit;

import com.example.myapplication.model.listData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DealsApi {

    @GET("/api/deals/listDeals")
    Call<List<listData>> getListDeals();

}
