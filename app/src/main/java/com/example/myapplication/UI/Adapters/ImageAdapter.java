package com.example.myapplication.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<String> imagePaths;
    private final Context context;

    // Add a boolean to track whether to show default images
    private boolean showDefaultImages = true;

    // Default placeholder images
    private static final int[] DEFAULT_IMAGES = {
            R.drawable.imagedef,
            R.drawable.imagedef,
            R.drawable.imagedef
    };

    public ImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (showDefaultImages) {
            // Show default placeholder images
            holder.imageView.setImageResource(DEFAULT_IMAGES[position % DEFAULT_IMAGES.length]);
        } else {
            // Load user-uploaded images using Glide
            String imagePath = imagePaths.get(position);
            Glide.with(context).load(imagePath).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return showDefaultImages ? DEFAULT_IMAGES.length : imagePaths.size();
    }

    public void setShowDefaultImages(boolean showDefaultImages) {
        this.showDefaultImages = showDefaultImages;
        notifyDataSetChanged(); // Notify adapter to refresh the view
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.selectedImageView);
        }
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }
}
