package com.example.myapplication.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor {//implements Interceptor {
  /*  private final Context context;

    public RequestInterceptor(Context context) {
        this.context = context;
    }

    // Method to retrieve the token from SharedPreferences
    private String getJwtToken() {
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return preferences.getString("jwtToken", "");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Add the token to the request headers
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + getJwtToken())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }*/
}
