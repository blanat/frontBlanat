/*
package com.example.myapplication.UI.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.listData;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class listAdapter extends ArrayAdapter<listData>{
    public listAdapter(@NonNull Context context, ArrayList<listData> dataArrayList) {
        super(context, R.layout.list_deals,dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
         listData listData = getItem(position);
         if (view == null) {
             view = LayoutInflater.from(getContext()).inflate(R.layout.list_deals, parent, false);
         }

        ShapeableImageView listImage = view.findViewById(R.id.listImage);
        TextView listTitle = view.findViewById(R.id.listtitre);
        TextView listTime = view.findViewById(R.id.listTime);


        ImageButton minusButton = view.findViewById(R.id.moins);
        TextView voteCount = view.findViewById(R.id.voteCount);
        ImageButton plusButton = view.findViewById(R.id.plus);


        TextView listDesc = view.findViewById(R.id.listdesc);

        TextView priceN = view.findViewById(R.id.prixN);
        TextView priceA = view.findViewById(R.id.prixA);
        ImageView livraisonIcon = view.findViewById(R.id.livraisonIcon);

        Button listButton = view.findViewById(R.id.listButton);


        listImage.setImageResource(listData.getImage());
        listTitle.setText(listData.getTitre());
        listTime.setText(listData.getTime());
        listDesc.setText(listData.getDesc());
        priceN.setText(listData.getPrixN());
        priceA.setText(listData.getPrixA());

        // Vérifiez si la livraison est disponible avant de définir la visibilité
        if (listData.getLivraison() != null && listData.getLivraison()) {
            livraisonIcon.setVisibility(View.VISIBLE);
        } else {
            livraisonIcon.setVisibility(View.INVISIBLE);
        }


        // Set onClickListener for buttons if needed
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });

        // Set onClickListener for listButton if needed
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });



        return view;


    }


}
*/
