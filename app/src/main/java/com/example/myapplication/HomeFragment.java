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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements selectListener {

    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayAdapter<listData> dealsAdapter;


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
        editText = view.findViewById(R.id.edittext);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        loadDeals();
        setupSearchFunctionality();

        return view;
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
        RetrofitService retrofitService = new RetrofitService(requireContext());
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);
        dealsApi.getListDeals()
                .enqueue(new Callback<List<listData>>() {
                    @Override
                    public void onResponse(Call<List<listData>> call, Response<List<listData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateListView(response.body());
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

            // Utilisez un ArrayAdapter pour prendre en charge la fonction de filtrage
            dealsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dealslist);
            recyclerView.setAdapter(new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // À remplacer par votre implémentation de onCreateViewHolder
                    return null;
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    // À remplacer par votre implémentation de onBindViewHolder
                }

                @Override
                public int getItemCount() {
                    return 0;
                }
            });
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

}
