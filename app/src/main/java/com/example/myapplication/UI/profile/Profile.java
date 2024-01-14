package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    private RetrofitService retrofitService;
    private UserApi userApi;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView paramsLink = findViewById(R.id.paramId);

        paramsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Profile.this, Parameter.class);
                signupIntent.putExtra("email", email);
                signupIntent.putExtra("password", password);
                startActivity(signupIntent);
            }
        });

        // Call the new method to load deals
        loadDealsDTOByUserId();
    }

    private void loadDealsDTOByUserId() {
        // Load user email using callback
        loadUserFromToken(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String userEmail = response.body();
                    // Use the user email in the deals API call
                    performDealsApiCall(userEmail);
                } else {
                    Log.e("Profile", "Error loading user email: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Profile", "Error loading user email: " + t.getMessage());
                // Handle the failure case
            }
        });
    }

    private void performDealsApiCall(String userEmail) {
        RetrofitService retrofitService = new RetrofitService(this);
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);

        dealsApi.getListDealsDTOByUserEmail(userEmail)
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateListView(response.body());
                        } else {
                            Log.e("Profile", "Error loading deals: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<listData>> call, Throwable t) {
                        Log.e("Profile", "Error loading deals: " + t.getMessage());
                        // Handle the failure case
                    }
                });
    }

    private void populateListView(List<listData> dealslist) {
        Log.d("Profile", "populateListView called with " + dealslist.size() + " deals");
        // Implement your logic to update UI with the list of deals
    }

    private String retrieveToken() {
        // Retrieve the token from SharedPreferences using the Activity's context
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }

    private void loadUserFromToken(final Callback<String> userCallback) {
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String token = retrieveToken();

        userApi.getUserFromToken(token)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String userEmail = response.body();
                            userCallback.onResponse(call, Response.success(userEmail));
                        } else {
                            userCallback.onFailure(call, new Throwable("Error loading user information: " + response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        userCallback.onFailure(call, new Throwable("Error loading user information: " + t.getMessage()));
                    }
                });
    }

    private void handleApiError(String errorMessage) {
        Log.e("Profile", errorMessage);
        // Handle the error, e.g., show an error message to the user
    }
}
