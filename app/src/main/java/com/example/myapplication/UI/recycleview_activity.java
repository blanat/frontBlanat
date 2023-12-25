package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.DealsAdapter;
import com.example.myapplication.UI.Adapters.selectListener;
import com.example.myapplication.UI.details.DetailsDealActivity;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recycleview_activity extends AppCompatActivity implements selectListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        recyclerView = findViewById(R.id.deals_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDeals();
    }

    private void loadDeals() {
        RetrofitService retrofitService = new RetrofitService(this);
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDeals()
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateListView(response.body());
                        } else {
                            Toast.makeText(recycleview_activity.this, "Failed to load Deals", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<listData>> call, Throwable t) { // Corrected parameter list here
                        Toast.makeText(recycleview_activity.this, "Failed to load Deals", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void populateListView(List<listData> dealslist) {
        DealsAdapter dealsAdapter = new DealsAdapter(dealslist, this);
        recyclerView.setAdapter(dealsAdapter);
    }

    @Override
    public void onItemClicked(listData deal) {
        // Handle the item click
        // Navigate to the details activity and pass the clicked listData

        Intent intent = new Intent(this, DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }



}

