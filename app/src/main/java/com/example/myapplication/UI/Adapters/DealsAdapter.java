package com.example.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.listData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsHolder> {

    private final List<listData> dealslist;

    public DealsAdapter(List<listData> dealsList) {
        this.dealslist = dealsList;
    }




    @NonNull
    @Override
    public DealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_deals, parent, false);
        return new DealsHolder(view); // Create and return a new DealsHolder
    }


    @Override
    public void onBindViewHolder(@NonNull DealsHolder holder, int position) {
        listData deals = dealslist.get(position);

        // Load image using Picasso
        Picasso.get().load(deals.getfirstImageURL()).into(holder.Image);

        holder.Titre.setText(deals.getTitle());
        holder.listDesc.setText(deals.getDescription());
        holder.listTime.setText(deals.getTimePassedSinceCreation());
        holder.priceA.setText(deals.getPrix_A());
        holder.priceN.setText(deals.getPrix_N());
        holder.voteCount.setText(String.valueOf(deals.getDeg()));

        if (deals.getLivraisonExist()) {
            holder.livraisonIcon.setImageResource(R.drawable.livraison);
            holder.livraisonIcon.setVisibility(View.VISIBLE);
        } else {
            holder.livraisonIcon.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return dealslist.size();
    }
}
