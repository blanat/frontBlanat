package com.example.myapplication.retrofit;

import com.example.myapplication.model.JwtAuthenticationResponse;
import com.example.myapplication.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @POST("/api/authentication/signin")
    Call<JwtAuthenticationResponse> signin(@Body User user);

    @POST("/api/authentication/signup")
    Call<User> signup(@Body User user);

    @PUT("/api/user/{email}/password")
    Call<User> updatePassword(@Path("email") String email, @Body String newPassword);

    @DELETE("/api/user/{email}")
    Call<Void> deleteUser(@Path("email") String email);

}
