package com.example.myapplication;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.UI.Adapters.DealsAdapter;
import com.example.myapplication.UI.Adapters.selectListener;
import com.example.myapplication.UI.details.DetailsDealActivity;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.google.android.material.tabs.TabLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements selectListener {

    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayAdapter<listData> dealsAdapter;

    private List<listData> dealslist;
    RetrofitService retrofitService;


    //=========================
    private TabLayout tabLayout;
    private int selectedTabIndex = 0; // Default selected tab index

    //=========================


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


        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);

        // Set the title to the desired string
        toolbar.setTitle("Blanat");


        recyclerView = view.findViewById(R.id.deals_recycleview);
        editText = view.findViewById(R.id.edittext);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        retrofitService = new RetrofitService(requireContext());

        //=====================Changes for sort:==========================

        tabLayout = view.findViewById(R.id.tabLayout);

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabIndex = tab.getPosition();
                loadDeals(); // Load deals based on the selected tab
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Load deals initially
        loadDeals();

        setupSearchFunctionality();

        return view;
        /*loadDeals();
        return view;*/
    }
    private void setupSearchFunctionality() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrez la liste en temps réel pendant que l'utilisateur tape
                if (dealsAdapter != null) {
                    dealsAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Vous pouvez ajouter une logique ici si nécessaire après la saisie du texte
            }
        });
    }


    private void loadDeals() {
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDeals()
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            dealslist = response.body();
                            sortDeals();
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
                Log.d("HomeFragment", "Deal: " + deal.getTitle());
            }

            // Use the DealsAdapter directly
            DealsAdapter dealsAdapter = new DealsAdapter(dealslist, this);
            recyclerView.setAdapter(dealsAdapter);


            // Update the adapter when the text in the EditText changes
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    dealsAdapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        } else {
            ImageView noDataImage = requireView().findViewById(R.id.noDataImage);

            noDataImage.setVisibility(View.VISIBLE);
            Log.e("HomeFragment", "Deals list is empty");
        }
    }

    private void sortDeals() {
        // Sort the deals based on the selected tab
        switch (selectedTabIndex) {
            case 0:
                // Recently Created sorting logic
                Collections.sort(dealslist, Comparator.comparing(this::parseTimePassedSinceCreation));

                break;
            case 1:
                // Highest Degree sorting logic
                Collections.sort(dealslist, Comparator.comparingInt(listData::getDeg).reversed());
                break;
            case 2:
                // Most Commented sorting logic
                Collections.sort(dealslist, Comparator.comparingInt(listData::getNumberOfComments).reversed());

                break;

            // Add more cases for additional tabs if needed
        }
    }

    private int parseTimePassedSinceCreation(listData deal) {
        // Assuming timePassedSinceCreation is in the format "Xd:Yh:Zmin"
        String timeString = deal.getTimePassedSinceCreation();

        // Extract days, hours, and minutes from the string
        String[] parts = timeString.split(":");
        int days = 0;
        int hours = 0;
        int minutes = 0;

        for (String part : parts) {
            if (part.contains("d")) {
                days = Integer.parseInt(part.split("d")[0]);
            } else if (part.contains("h")) {
                hours = Integer.parseInt(part.split("h")[0]);
            } else if (part.contains("min")) {
                minutes = Integer.parseInt(part.split("min")[0]);
            }
        }

        // Calculate total minutes
        return days * 24 * 60 + hours * 60 + minutes;
    }







    //deals deg code
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
                    dealslist.get(position).setDeg(dealslist.get(position).getDeg() - 1);
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
