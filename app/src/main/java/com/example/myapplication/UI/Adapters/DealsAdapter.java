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
    private selectListener listener;

    public DealsAdapter(List<listData> dealsList,selectListener listener) {
        this.dealslist = dealsList;
        this.listener = listener;
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
        Picasso.get().load(deals.getFirstImageUrl()).into(holder.Image);

        holder.Titre.setText(deals.getTitle());
        holder.listDesc.setText(deals.getDescription());
        holder.listTime.setText(deals.getTimePassedSinceCreation());
        holder.priceA.setText(String.valueOf((int) deals.getPrice()));
        holder.priceN.setText(String.valueOf((int) deals.getNewPrice()));

        holder.voteCount.setText(String.valueOf(deals.getDeg()));

        if (deals.isDeliveryExist()) {
            holder.livraisonIcon.setImageResource(R.drawable.livraison);
            holder.livraisonIcon.setVisibility(View.VISIBLE);
        } else {
            holder.livraisonIcon.setVisibility(View.INVISIBLE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(dealslist.get(clickedPosition));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dealslist.size();
    }
}
