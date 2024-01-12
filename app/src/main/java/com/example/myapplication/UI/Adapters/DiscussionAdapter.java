package com.example.myapplication.UI.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.Discussion;
import com.example.myapplication.picasso.CircleTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class DiscussionAdapter extends ArrayAdapter<Discussion> {

    public DiscussionAdapter(Context context, List<Discussion> discussionItemList) {
        super(context, 0, discussionItemList);
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
            }else {
                // Handle the case where getCategories() returns null
                txtCategorie.setText("Unknown Category");
            }
            // Check if the user associated with the discussion is not null

            if (discussionItem.getCreateurUsername()!= null) {
                // Set the user's name to txtCreateur, with a null check
                String userName = discussionItem.getCreateurUsername();
                txtCreateur.setText(userName != null ? userName : "Unknown User");
            }else {
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

        return convertView;
    }


}
