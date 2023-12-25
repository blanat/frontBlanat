package com.example.myapplication.UI.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.DealComment;
import com.example.myapplication.model.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.List;
// Inside CommentsAdapter.java

// Import statements...

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<DealComment> comments;

    public CommentsAdapter(List<DealComment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_items, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        DealComment comment = comments.get(position);

        // Set comment content
        holder.commentContentTextView.setText(comment.getContent());

        // Set comment timestamp
        holder.commentTimestampTextView.setText(comment.getTimeSincePosted());

        // Set user details (profile image and username)

        UserDTO sender = comment.getSender();
        Log.d("image ============", "Loading image : " + sender.getProfileImageUrl());

        if (sender != null) {
            // Use Picasso to load the profile image
            Picasso.get()
                    .load(sender.getProfileImageUrl())
                    .resize(50, 50) // Resize the image if needed
                    .centerCrop() // Center crop the image
                    .into(holder.userProfileImageView);

            // Set username
            holder.usernameTextView.setText(sender.getUserName());
            Log.d("username ============", "Loading username : " + sender.getUserName());


        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentContentTextView;
        TextView commentTimestampTextView;
        ImageView userProfileImageView;
        TextView usernameTextView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentContentTextView = itemView.findViewById(R.id.commentContentTextView);
            commentTimestampTextView = itemView.findViewById(R.id.commentTimestampTextView);
            userProfileImageView = itemView.findViewById(R.id.commentUserProfileImage);
            usernameTextView = itemView.findViewById(R.id.commentUsernameTextView);
        }
    }
}
