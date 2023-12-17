package com.example.myapplication.retrofit;

import com.example.myapplication.model.User;
import com.example.myapplication.model.listData;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DealAPI {

    @POST("deals")
    Call<listData> signin(@Body User user);
}
