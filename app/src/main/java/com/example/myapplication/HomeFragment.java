package com.example.myapplication;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private List<listData> dealslist;
    RetrofitService retrofitService;

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

        retrofitService = new RetrofitService(requireContext());

        loadDeals();



        return view;
    }

    private void loadDeals() {
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDeals()
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            dealslist = response.body();
                            populateListView(dealslist);
                        } else {
                            Log.e("HomeFragment", " erreur : ");
                       }
                    }

                    @Override
                    public void onFailure(Call<List<listData>> call, Throwable t) {
                        // Handle the failure case
                    }
                });
    }

    private void populateListView(List<listData> dealslist) {
        Log.d("HomeFragment", "populateListView called with " + dealslist.size() + " deals");
        if (!dealslist.isEmpty()) {
            for (listData deal : dealslist) {
                Log.d("HomeFragment", "Deal: " + deal.getTitle()); // Ajoutez des logs pour chaque propriété que vous voulez vérifier
            }
            DealsAdapter dealsAdapter = new DealsAdapter(dealslist, this);
            recyclerView.setAdapter(dealsAdapter);
        } else {
            Log.e("HomeFragment", "Deals list is empty");
        }
    }



    @Override
    public void onItemClicked(listData deal) {
        // Handle the item click
        // Navigate to the details activity and pass the clicked listData

        Intent intent = new Intent(requireContext(), DetailsDealActivity.class);
        intent.putExtra("deal", deal);
        startActivity(intent);
    }

// Inside your HomeFragment class

    @Override
    public void onPlusButtonClicked(int position) {
        //if (!dealslist.get(position).hasInteracted()) {
            incrementDegree(dealslist.get(position).getDealID(), position);
            //dealslist.get(position).setInteracted(true);
        //} else {
            // User has already interacted, show a message or handle accordingly
        //    Log.d("DealsAdapter", "User has already interacted with this deal");
        //}
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
                    Log.e("HomeFragment", "Failed to increment degree");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("HomeFragment", "Error while calling incrementDeg API", t);
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
                    Log.e("HomeFragment", "Failed to decrement degree");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("HomeFragment", "Error while calling decrementDeg API", t);
            }
        });
    }

}
