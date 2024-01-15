package com.example.myapplication.retrofit;

import com.example.myapplication.model.Discussion;
import com.example.myapplication.model.JwtAuthenticationResponse;
import com.example.myapplication.model.MessageDTO;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserProfileStatisticsDTO;
import com.example.myapplication.model.listData;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @POST("/api/authentication/signin")
    Call<JwtAuthenticationResponse> signin(@Body User user);

    @POST("/api/authentication/signup")
    Call<JwtAuthenticationResponse> signup(@Body User user);


    @PUT("/api/users/{email}")
    Call<User> updatePassword(@Path("email") String email, @Body String newPassword);

    @DELETE("/api/users/{email}/password")
    Call<Void> deleteUser(@Path("email") String email);

    @GET("/api/users/userFromToken2")
    Call<User> fromToke(@Header("Authorization") String authorizationHeader);

    @GET("/api/users/getUserFromToken")
    Call<User> getUserFromToken(@Header("Authorization") String token);

    @POST("/api/users/userDetails/{email}")
    Call<UserProfileStatisticsDTO> getUserDetails(@Path("email") String email);


    @GET("/api/discussions/getAllDiscussions")
    Call<List<Discussion>> getAllDiscussionsInfo();


    @PUT("/{discussionId}/update-views")
    Call<Void> updateViews(@Path("discussionId") Long discussionId);

    @GET("/api/discussions/{discussionId}/comments")
    Call<List<MessageDTO>> getCommentsByDiscussionId(@Path("discussionId") Long discussionId);


    @POST("/api/discussions/{discussionId}/messages")
    Call<MessageDTO> addMessage(@Path("discussionId") Long discussionId, @Body MessageDTO messageDTO);

    @POST("/api/discussions/create")
    Call<Discussion> createDiscussion(@Body Discussion discussionDTO);

    @GET("/api/users/extractUserName")
    Call<String> ExtractUserName(@Header("Authorization") String token);

    @PUT("/api/discussions/{discussionId}/update-views")
    Call<Long> updateViews(@Path("discussionId") Long discussionId, @Header("Authorization") String token);

    @GET("/api/discussions/created-by-current-user")
    Call<List<Discussion>> getDiscussionsCreatedByCurrentUser();

    @DELETE("/api/discussions/{discussionId}")
    Call<Void> deleteDiscussionAndMessages(@Path("discussionId") Long discussionId);

    @POST("/api/discussions/{discussionId}/updateSave")
    Call<Integer> updateSave(@Path("discussionId") Long discussionId);
}