package com.example.myapplication.UI;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.DetailsImagesAdapter;
import com.example.myapplication.model.UserDTO;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;
import com.squareup.picasso.Picasso;

import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detailsactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsactivity);

        // Receive Intent data
        Intent intent = getIntent();
        listData deal = intent.getParcelableExtra("deal");

        // Call the method to load image URLs
        if (deal != null) {
            loadImageUrls(deal.getDealID());
        }

        // Update UI
        TextView titleTextView = findViewById(R.id.listtitre);
        TextView prixnvTextView = findViewById(R.id.prixnv);
        TextView prixanTextView = findViewById(R.id.prixan);
        TextView prixliTextView = findViewById(R.id.prixli);

        Button dealButton = findViewById(R.id.DealButton);

        TextView dealDetailTextView = findViewById(R.id.dealdetail);
        TextView localDealTextView = findViewById(R.id.localdeal);

        // Added: User details
        ImageView userProfileImageView = findViewById(R.id.userProfileImage);
        TextView usernameTextView = findViewById(R.id.usernameTextView);

        // Check if deal is not null before accessing its properties
        if (deal != null) {
            titleTextView.setText(deal.getTitle());

            // Set the prices
            prixnvTextView.setText(String.format("%s %s", deal.getNewPrice(), "DH"));
            prixanTextView.setText(String.format("%s %s", deal.getPrice(), "DH"));

            // Set the delivery icon visibility based on deliveryExist
            ImageView iconelivImageView = findViewById(R.id.iconeliv);
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
                // Note: You need to load the image using an image loading library like Glide or Picasso
                // Example using Glide:
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
    }


    private void loadImageUrls(long dealId) {
        RetrofitService retrofitService = new RetrofitService(this);
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
                    Toast.makeText(Detailsactivity.this, "Failed to get image URLs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Handle failure
                Toast.makeText(Detailsactivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView(List<String> imageUrls) {
        // Set up your RecyclerView with the imageUrls using the DetailsImagesAdapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        DetailsImagesAdapter adapter = new DetailsImagesAdapter(Detailsactivity.this, imageUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(Detailsactivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

}

