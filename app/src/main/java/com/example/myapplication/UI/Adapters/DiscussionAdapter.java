package com.example.myapplication.UI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.UI.DiscussionFragment;
import com.example.myapplication.model.Discussion;
import com.example.myapplication.picasso.CircleTransform;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionAdapter extends ArrayAdapter<Discussion> {
    private SharedPreferences sharedPreferences;

    public DiscussionAdapter(Context context, List<Discussion> discussionItemList) {
        super(context, 0, discussionItemList);
        sharedPreferences = context.getSharedPreferences("DiscussionAdapterPrefs", Context.MODE_PRIVATE);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the current discussion item
        Discussion discussionItem = getItem(position);


        // Récupérez l'URL de l'image de profil depuis la discussion
        String profileImageUrl = discussionItem.getProfileImageUrl();

        // Check if convertView is null and inflate the layout if needed
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_discussion, parent, false);
        }
        // Find ImageView from the inflated layout (convertView)
        ImageView imageGroupOne = convertView.findViewById(R.id.imageGroupOne);

        // Utilisez Picasso pour charger l'image dans votre ImageView

        // Utilisez Picasso pour charger l'image dans votre ImageView
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Picasso.get()
                    .load(profileImageUrl)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.img_group) // Image par défaut si l'URL est vide ou invalide
                    .error(R.drawable.img_group) // Image à afficher en cas d'erreur de chargement
                    .fit()
                    .centerCrop()
                    .into(imageGroupOne, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            // L'image est chargée avec succès
                            Log.d("Picasso", "Image chargée avec succès: " + profileImageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Erreur lors du chargement de l'image
                            Log.e("Picasso", "Erreur lors du chargement de l'image: " + profileImageUrl);
                        }
                    });
        } else {
            // Si l'URL de l'image est vide ou invalide, chargez une image par défaut ou un espace réservé
            imageGroupOne.setImageResource(R.drawable.img_group); // Charger l'image par défaut
        }


        Log.d("DiscussionAdapter1", "Discussion: " + discussionItem);
        Log.d("DiscussionAdapter2", "Discussion: " + discussionItem.getCreateurUsername());
        // Check if convertView is null and inflate the layout if needed
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_discussion, parent, false);
        }

        // Check if discussionItem is not null
        if (discussionItem != null) {
            Log.d("creator", "Createur: " + discussionItem.getCreateurUsername());
            TextView txtTitre = convertView.findViewById(R.id.txtTitre);
            TextView txtCategorie = convertView.findViewById(R.id.txtCategorie);
            TextView txtCreateur = convertView.findViewById(R.id.txtCreateur);
            TextView txtNbrVue = convertView.findViewById(R.id.txtNbrVue);

            // Set values to TextViews, with null checks
            txtTitre.setText(discussionItem.getTitre());

            // Assuming getCategories() returns an Enum instance, you can use name() to get the enum name
            if (discussionItem.getCategories() != null) {
                txtCategorie.setText(discussionItem.getCategories().name());
            } else {
                // Handle the case where getCategories() returns null
                txtCategorie.setText("Unknown Category");
            }
            // Check if the user associated with the discussion is not null

            if (discussionItem.getCreateurUsername() != null) {
                // Set the user's name to txtCreateur, with a null check
                String userName = discussionItem.getCreateurUsername();
                txtCreateur.setText(userName != null ? userName : "Unknown User");
            } else {
                // Handle the case where the user is null
                txtCreateur.setText("Unknown USERNAME");
            }
            int nbrVue = discussionItem.getNbrvue();
            if (nbrVue >= 0) {
                txtNbrVue.setText(String.valueOf(nbrVue));
            } else {
                // Handle the case where nbrVue is less than 0 (or handle it according to your application's logic)
                txtNbrVue.setText("0"); // or any default value you prefer
            }


        }
        // Get the discussion item for the current position
        final Discussion discussion = getItem(position);

// Find the ImageView in your item_discussion layout
        ImageView imageVector = convertView.findViewById(R.id.imageVector);

// Set the icon based on the saved state in SharedPreferences
        boolean savedState = sharedPreferences.getBoolean("saveState_" + discussion.getId(), false);
        if (savedState) {
            imageVector.setImageResource(R.drawable.img_vector);
        } else {
            imageVector.setImageResource(R.drawable.avantsaveicon);
        }

// Set an OnClickListener on the ImageView
        imageVector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                if (discussion != null) {
                    // Call the updateSave function and update the icon based on the result
                    updateSave(discussion, imageVector);
                }
            }
        });



        return convertView;
    }

    private void updateSave(Discussion discussion, ImageView imageVector) {
        // Retrofit setup
        RetrofitService retrofitService = new RetrofitService(getContext());

        // Create an instance of RequestInterceptor with the token

        // Pass the interceptor to Retrofit setup
        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        // Make a POST request to the updateSave endpoint
        Call<Integer> call = userApi.updateSave(discussion.getId());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    // Discussion save updated successfully, update UI or perform any other actions
                    Integer updatedDiscussion = response.body();
                    Log.d("save", "save" + response.body());
                    // Check the updated save value and update the icon accordingly
                    if (updatedDiscussion != null) {
                        // Change the icon to img_vector
                        imageVector.setImageResource(R.drawable.img_vector);

                        // Save the updated state in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("saveState_" + discussion.getId(), true);
                        editor.apply();
                    } else {
                        // Change the icon to the default icon (or handle other cases)
                        imageVector.setImageResource(R.drawable.img_group);

                        // Save the updated state in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("saveState_" + discussion.getId(), false);
                        editor.apply();
                    }

                    // Notify the adapter that the data set has changed
                    notifyDataSetChanged();

                    // You may want to handle the updated discussion accordingly
                    Log.d("DiscussionAdapter", "Save updated for discussion: " + updatedDiscussion);
                } else {
                    // ... (unchanged)
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Handle the failure
                Log.e("DiscussionAdapter", "Network error: " + t.getMessage());
            }
        });

    }
}

