package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.UI.Adapters.SavedDealsAdapter;
import com.example.myapplication.UI.Adapters.selectListener;
import com.example.myapplication.UI.details.DetailsDealActivity;
import com.example.myapplication.model.User;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MydealsFragment extends Fragment implements selectListener {

    private RetrofitService retrofitService;

    private RecyclerView recyclerView;
    private String email;


    public MydealsFragment() {
        // Required empty public constructor
    }


    public static MydealsFragment newInstance() {
        MydealsFragment fragment = new MydealsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mydeals, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Correct initialization of retrofitService
        retrofitService = new RetrofitService(requireContext());

        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String jwtToken = retrieveToken();

        userApi.fromToke("Bearer " + jwtToken)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
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

        return view;
    }

    private String retrieveToken() {
        // Retrieve the token from SharedPreferences using the Activity's context
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
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
            com.example.myapplication.UI.Adapters.SavedDealsAdapter dealsAdapter = new SavedDealsAdapter(dealsList, this);
            recyclerView.setAdapter(dealsAdapter);


        }
    }

    public void onItemClicked(listData deal) {
        Intent intent = new Intent(requireContext(), DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }

    @Override
    public void onPlusButtonClicked(int position) {

    }

    @Override
    public void onMoinsButtonClicked(int position) {

    }
}