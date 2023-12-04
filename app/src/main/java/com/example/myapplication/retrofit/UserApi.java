package com.example.myapplication.retrofit;

import com.example.myapplication.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/signin")
    Call<User> signin();

    @POST("/signup")
    Call<User> signup(@Body User user);

}
