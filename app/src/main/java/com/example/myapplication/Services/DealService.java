package com.example.myapplication.Services;

import android.content.Context;

import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DealService {

    private final DealApi dealApi;

    public DealService(Context context) {
        RetrofitService retrofitService = new RetrofitService(context);
        dealApi = retrofitService.getRetrofit().create(DealApi.class);
    }



    public void uploadDeal(RequestBody deal, List<String> imagePaths, Callback<RequestBody> callback) {
        List<MultipartBody.Part> imageParts = prepareImageParts(imagePaths);

        Call<RequestBody> call = dealApi.uploadDeal(deal, imageParts);
        call.enqueue(callback);
    }

    private List<MultipartBody.Part> prepareImageParts(List<String> imagePaths) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();

        for (String imagePath : imagePaths) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), new File(imagePath));
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", "image.jpg", fileBody);
            imageParts.add(imagePart);
        }

        return imageParts;
    }
}
