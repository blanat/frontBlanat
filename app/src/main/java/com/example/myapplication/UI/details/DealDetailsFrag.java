package com.example.myapplication.UI.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.DetailsImagesAdapter;
import com.example.myapplication.model.UserDTO;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealDetailsFrag extends Fragment {

    private static final String ARG_DEAL = "arg_deal";
    private listData deal;
    private View view;
    private RecyclerView recyclerView;



    public DealDetailsFrag() {
        // Required empty public constructor
    }

    public static DealDetailsFrag newInstance(listData deal) {
        DealDetailsFrag fragment = new DealDetailsFrag();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEAL, deal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deal = getArguments().getParcelable(ARG_DEAL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deal_details, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        if (deal != null) {
            loadImageUrls(deal.getDealID());
        }
        // Update UI
        TextView titleTextView = view.findViewById(R.id.listtitre);
        TextView prixnvTextView = view.findViewById(R.id.prixnv);
        TextView prixanTextView = view.findViewById(R.id.prixan);
        TextView prixliTextView = view.findViewById(R.id.prixli);

        Button dealButton = view.findViewById(R.id.DealButton);

        TextView dealDetailTextView = view.findViewById(R.id.dealdetail);
        TextView localDealTextView = view.findViewById(R.id.localdeal);

        // Added: User details
        ImageView userProfileImageView = view.findViewById(R.id.userProfileImage);
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);

        // Check if deal is not null before accessing its properties
        if (deal != null) {
            titleTextView.setText(deal.getTitle());

            // Set the prices
            prixnvTextView.setText(String.format("%s %s", deal.getNewPrice(), "DH"));
            prixanTextView.setText(String.format("%s %s", deal.getPrice(), "DH"));

            // Set the delivery icon visibility based on deliveryExist
            ImageView iconelivImageView = view.findViewById(R.id.iconeliv);
            iconelivImageView.setVisibility(deal.isDeliveryExist() ? View.VISIBLE : View.GONE);

            // Set the delivery price if delivery exists
            if (deal.isDeliveryExist()) {
                prixliTextView.setText(String.format("%s %s", deal.getDeliveryPrice(), "DH"));
            } else {
                // If no delivery, hide the delivery TextView
                prixliTextView.setVisibility(View.GONE);
            }

            // Set the deal details
            dealDetailTextView.setText(deal.getDescription());
            localDealTextView.setText(String.format("Localisation: %s", deal.getLocalisation()));

            // Added: Set user details
            UserDTO dealCreator = deal.getDealCreator();
            if (dealCreator != null) {
                // Set the user profile image
                Picasso.get().load(dealCreator.getProfileImageUrl()).into(userProfileImageView);

                // Set the username
                usernameTextView.setText(dealCreator.getUserName());
            }

            dealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if deal is not null before accessing its properties
                    if (deal != null && deal.getLienDeal() != null) {
                        // Open the web link in a browser
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deal.getLienDeal()));
                        startActivity(browserIntent);
                    }
                }
            });
        }




        return view;
    }
// ...

    private void loadImageUrls(long dealId) {
        RetrofitService retrofitService = new RetrofitService(getContext()); // Use getContext() here
        DealApi dealsApi = retrofitService.getRetrofit().create(DealApi.class);

        // Corrected: Call the API method and enqueue the call
        Call<List<String>> call = dealsApi.getImageUrlsForDeal(dealId);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> imageUrls = response.body();
                    // Now you can use imageUrls to populate your RecyclerView
                    initRecyclerView(imageUrls);
                } else {
                    // Handle error
                    Toast.makeText(getContext(), "Failed to get image URLs", Toast.LENGTH_SHORT).show(); // Use getContext() here
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Handle failure
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show(); // Use getContext() here
            }
        });
    }
    // Modify the initRecyclerView method to use the class field
    private void initRecyclerView(List<String> imageUrls) {
        DetailsImagesAdapter adapter = new DetailsImagesAdapter(getContext(), imageUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

}
