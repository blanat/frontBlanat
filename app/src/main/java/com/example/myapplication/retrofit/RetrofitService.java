package com.example.myapplication.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;
    private Context context;

    public RetrofitService(Context context) {
        this.context = context;
        initializeRetrofit();
    }


    private void initializeRetrofit() {
        // Retrieve the token from SharedPreferences
        String jwtToken = getJwtToken();

        // Create a Gson instance
        Gson gson = new GsonBuilder().create();
        Log.d("Token", jwtToken);

        // Create an OkHttpClient with an Authorization header
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();
                    okhttp3.Request newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + jwtToken)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        // Build the Retrofit instance with the OkHttpClient and GsonConverterFactory
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.11.113:8085")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    private String getJwtToken() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("token", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
