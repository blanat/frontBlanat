package com.example.myapplication.retrofit;

import com.example.myapplication.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/api/authentication/signin")
    Call<User> signin();

    @POST("/api/authentication/signup")
    Call<User> signup(@Body User user);

}
