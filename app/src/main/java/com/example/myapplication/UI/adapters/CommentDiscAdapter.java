package com.example.myapplication.Adapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.model.MessageDTO;
import com.example.myapplication.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentDiscAdapter extends ArrayAdapter<MessageDTO> {

    public CommentDiscAdapter(Context context, int resource) {
        super(context, resource);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_comments_discussion, parent, false);
        }

        MessageDTO comment = getItem(position);

        if (comment != null) {
            TextView userNameTextView = view.findViewById(R.id.userNameTextView);
            TextView contentTextView = view.findViewById(R.id.contentTextView);
            TextView createdAtTextView = view.findViewById(R.id.createdAtTextView);
            ImageView userImageView = view.findViewById(R.id.userImageView);

            // Lier les données du commentaire à la vue
            contentTextView.setText(comment.getContent());
            createdAtTextView.setText(comment.getCreatedAt().toString());

            if (comment.getUserName() != null) {
                userNameTextView.setText(comment.getUserName());
            } else {
                userNameTextView.setText("Unknown username");
            }

            // Charger l'image à partir de l'URL fournie par MessageDTO en utilisant Picasso
            if (comment.getProfileImageUrl() != null && !comment.getProfileImageUrl().isEmpty()) {
                Picasso.get().load(comment.getProfileImageUrl())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.img_group)
                        .error(R.drawable.img_group) // Image à afficher en cas d'erreur de chargement
                        .fit()
                        .centerCrop()
                        .into(userImageView);
            } else {
                // Mettre une image par défaut si l'URL de l'image est vide ou invalide
                userImageView.setImageResource(R.drawable.img_group);
            }
        }

        return view;
    }
}

