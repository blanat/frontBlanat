package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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

        final CircleImageView profileImage = findViewById(R.id.profile_image);
        final TextView usernameTextView = findViewById(R.id.textView8);

        loadUserFromToken(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Update UI with user data
                    usernameTextView.setText(user.getUserName());
                    // Load image using your preferred image loading library
                    // For example, using Glide:
                    Glide.with(Profile.this)
                            .load(user.getImage())
                            .placeholder(R.drawable.login)  // Placeholder image
                            .into(profileImage);
                } else {
                    Log.e("YourTag", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("YourTag", "Failure: " + t.getMessage());
            }
        });

        final TextView paramsLink = findViewById(R.id.paramId);

        paramsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Profile.this, Parameter.class);
                startActivity(signupIntent);
            }
        });
    }


//    private void loadDealsDTOByUserId() {
//        // Load user email using callback
//        loadUserFromToken(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    String userEmail = response.body();
//                    // Use the user email in the deals API call
//                    performDealsApiCall(userEmail);
//                } else {
//                    Log.e("Profile", "Error loading user email: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("Profile", "Error loading user email: " + t.getMessage());
//                // Handle the failure case
//            }
//        });
//    }

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

    private void loadUserFromToken(final Callback<User> userCallback) {
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.fromToke()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            // Call the callback with the User data
                            userCallback.onResponse(call, Response.success(user));
                        } else {
                            // Handle error and call the callback with the error
                            userCallback.onFailure(call, new IOException("Error: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Handle failure and call the callback with the failure
                        userCallback.onFailure(call, t);
                    }
                });
    }


    private void handleApiError(String errorMessage) {
        Log.e("Profile", errorMessage);
        // Handle the error, e.g., show an error message to the user
    }
}
