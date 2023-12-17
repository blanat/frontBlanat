package com.example.myapplication.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.listData;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsHolder> {

    private List<listData> dealslist;

    public DealsAdapter(List<listData> dealslist) {
        this.dealslist = dealslist;
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
            holder.Titre.setText(deals.getTitle());
            holder.listDesc.setText(deals.getDescription());
            holder.listTime.setText(deals.getTime());
            holder.priceA.setText(deals.getPrix_A());
            holder.priceN.setText(deals.getPrix_N());
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
