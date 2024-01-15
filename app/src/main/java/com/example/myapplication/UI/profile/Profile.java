package com.example.myapplication.UI.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.example.myapplication.UI.Adapters.SavedDealsAdapter;

import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.selectListener;
import com.example.myapplication.UI.details.DetailsDealActivity;
import com.example.myapplication.model.User;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements selectListener {
    private RetrofitService retrofitService;
    private UserApi userApi;
    private String email;
    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayAdapter<listData> SavedDealsAdapter;
    private List<listData> dealslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dealslist = new ArrayList<>();

        final TextView paramsLink = findViewById(R.id.paramId);
        final CircleImageView profileImage = findViewById(R.id.profile_image);
        final TextView username = findViewById(R.id.username);

        // Initialize your recyclerView and editText here
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        retrofitService = new RetrofitService(this);

        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String jwtToken = retrieveToken();


        userApi.fromToke("Bearer " + jwtToken)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            Uri fileUri = Uri.fromFile(new File(user.getProfileFilePath()));
                            String imageUrl = fileUri.toString();

                            Log.d("TAG", Uri.decode(imageUrl));
                            Picasso.get()
                                    .load(Uri.decode(imageUrl)) // Replace with the actual method to get the image URL
                                    .placeholder(R.drawable.default_image) // You can set a placeholder image while loading
                                    .error(R.drawable.default_image) // You can set an error image if the loading fails
                                    .into(profileImage);



                            username.setText(user.getUserName());
                            email = user.getEmail();
                            loadDeals();
                            Log.d("UserApiResponse", "User data: " + user.toString());
                        } else {
                            // Log the error
                            Log.e("UserApiResponse", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Log the failure
                        Log.e("UserApiFailure", "Failure: " + t.getMessage());
                    }
                });


        paramsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(Profile.this, Parameter.class);
                signupIntent.putExtra("email", email);
                startActivity(signupIntent);
            }
        });

//        loadDeals();
    }

    private String retrieveToken() {
        // Retrieve the token from SharedPreferences using the Activity's context
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }

    private void loadDeals() {
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDealsDTOByUserEmail(email).enqueue(new Callback<List<listData>>() {
            @Override
            public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<listData> dealsList = response.body();
                    Log.d("ProfileActivity", "Deals retrieved: " + dealsList.size());
                    populateListView(dealsList);
                } else {
                    Log.e("ProfileActivity", "Error loading deals");
                }
            }

            @Override
            public void onFailure(Call<List<listData>> call, Throwable t) {
                Log.e("ProfileActivity", "Failure loading deals: " + t.getMessage());
            }
        });
    }


    private void populateListView(List<listData> dealsList) {
        Log.d("ProfileActivity", "populateListView called with " + dealsList.size() + " deals");
        if (!dealsList.isEmpty()) {
            for (listData deal : dealsList) {
                Log.d("ProfileActivity", "Deal: " + deal.getTitle());
            }

            // Use the DealsAdapter directly
            SavedDealsAdapter dealsAdapter = new SavedDealsAdapter(dealsList, this);
            recyclerView.setAdapter(dealsAdapter);


        }
    }

    @Override
    public void onItemClicked(listData deal) {
        // Handle the item click
        // Navigate to the details activity and pass the clicked listData

        Intent intent = new Intent(this, DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }

    @Override
    public void onPlusButtonClicked(int position) {
        if (!dealslist.get(position).hasInteracted()) {
            incrementDegree(dealslist.get(position).getDealID(), position);
            dealslist.get(position).setInteracted(true);
        } else {
         //User has already interacted, show a message or handle accordingly
            Log.d("DealsAdapter", "User has already interacted with this deal");
        }
    }

    @Override
    public void onMoinsButtonClicked(int position) {
        if (!dealslist.get(position).hasInteracted()) {
            decrementDegree(dealslist.get(position).getDealID(), position);
            dealslist.get(position).setInteracted(true);
        } else {
            // User has already interacted, show a message or handle accordingly
            Log.d("DealsAdapter", "User has already interacted with this deal");
        }
    }

    private void incrementDegree(long dealId, int position) {
        DealApi dealApi = retrofitService.getRetrofit().create(DealApi.class);
        dealApi.incrementDeg(dealId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Increment the degree in the local list
                    dealslist.get(position).setDeg(dealslist.get(position).getDeg() + 1);
                    // Update the corresponding item in the adapter
                    recyclerView.getAdapter().notifyItemChanged(position);
                } else {
                    Log.e("Profile", "Failed to increment degree");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Profile", "Error while calling incrementDeg API", t);
            }
        });
    }

    private void decrementDegree(long dealId, int position) {
        DealApi dealApi = retrofitService.getRetrofit().create(DealApi.class);

        dealApi.decrementDeg(dealId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Decrement the degree in the local list
                    dealslist.get(position).setDeg(Math.max(0, dealslist.get(position).getDeg() - 1));
                    // Update the corresponding item in the adapter
                    recyclerView.getAdapter().notifyItemChanged(position);
                } else {
                    Log.e("Profile", "Failed to decrement degree");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Profile", "Error while calling decrementDeg API", t);
            }
        });
    }
}

