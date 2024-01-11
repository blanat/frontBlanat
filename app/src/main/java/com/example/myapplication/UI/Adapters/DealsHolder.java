package com.example.myapplication.UI.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.imageview.ShapeableImageView;

public class DealsHolder extends RecyclerView.ViewHolder {

    TextView Titre, listTime, voteCount, listDesc, priceN, priceA,commentCount,usernameTextView;
    ShapeableImageView Image;
    ImageView livraisonIcon, userProfileImageView;

    ConstraintLayout cardView;
    TextView plus, moins;

    public DealsHolder(@NonNull View itemView) {
        super(itemView);

        Image = itemView.findViewById(R.id.listImage);
        Titre = itemView.findViewById(R.id.listtitre);
        listTime = itemView.findViewById(R.id.listTime);

        voteCount = itemView.findViewById(R.id.voteCount);
        //ImageButton plusButton = itemView.findViewById(R.id.plus);
        //ImageButton moinsButton = itemView.findViewById(R.id.moins);


        commentCount = itemView.findViewById(R.id.commentCount);

        listDesc = itemView.findViewById(R.id.listdesc);
        priceN = itemView.findViewById(R.id.prixN);
        priceA = itemView.findViewById(R.id.prixA);
        livraisonIcon = itemView.findViewById(R.id.livraisonIcon);


        //for the onclick
        cardView = itemView.findViewById(R.id.cardView);

        plus = itemView.findViewById(R.id.plus);
        moins = itemView.findViewById(R.id.moins);

        // Added: User details
        userProfileImageView = itemView.findViewById(R.id.userProfileImage);
        usernameTextView = itemView.findViewById(R.id.usernameTextView);

/*
        // Vérifiez si la livraison est disponible avant de définir la visibilité
        if (listData.getLivraison() != null && listData.getLivraison()) {
            livraisonIcon.setVisibility(View.VISIBLE);
        } else {
            livraisonIcon.setVisibility(View.INVISIBLE);
        }*/

    }
}
