package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeFragment extends Fragment implements selectListener {

    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.deals_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadDeals();

        return view;
    }

    private void loadDeals() {
        RetrofitService retrofitService = new RetrofitService(requireContext());
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDeals()
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateListView(response.body());
                        } else {
                            // Handle the error case
                        }
                    }

                    @Override
                    public void onFailure(Call<List<listData>> call, Throwable t) {
                        // Handle the failure case
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

        Intent intent = new Intent(requireContext(), DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }

}
