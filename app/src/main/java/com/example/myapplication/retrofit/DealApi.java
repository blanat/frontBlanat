package com.example.myapplication.retrofit;

import com.example.myapplication.model.CommentRequest;
import com.example.myapplication.model.Deal;
import com.example.myapplication.model.DealComment;
import com.example.myapplication.model.listData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DealApi {
    // Define your API endpoints here
    @Multipart
    @POST("/api/deals")
    Call<RequestBody> uploadDeal(
            @Part("deal") RequestBody deal,
            @Part List<MultipartBody.Part> images
    );


    @GET("/api/deals")
    Call<List<listData>> getListDeals();

    @GET("/api/images/Deal/urls/{dealId}")
    Call<List<String>> getImageUrlsForDeal(@Path("dealId") long dealId);


    // Add the new endpoint for getting messages related to a deal
    @GET("/api/comments/deal/{dealId}")
    Call<List<DealComment>> getCommentsForDeal(@Path("dealId") long dealId);

    // Add the new endpoint for creating a comment related to a deal
    @POST("/api/comments/add")
    Call<ResponseBody> addComment(@Body CommentRequest commentRequest);



    @POST("/api/deals/{dealId}/increment")
    Call<Void> incrementDeg(@Path("dealId") long dealId);

    @POST("/api/deals/{dealId}/decrement")
    Call<Void> decrementDeg(@Path("dealId") long dealId);

}


