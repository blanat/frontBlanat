package com.example.myapplication.UI.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.CommentsAdapter;
import com.example.myapplication.model.DealComment;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.retrofit.RetrofitService;

import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CommentListFrag extends Fragment {

    private CommentsAdapter adapter;  // Assuming this is the adapter for your RecyclerView
    private RecyclerView recyclerViewComments;  // Assuming this is your RecyclerView


    private static final String ARG_DEAL_ID = "dealId";

    private long dealId;

    public CommentListFrag() {
        // Required empty public constructor
    }

    public static CommentListFrag newInstance(long dealId) {
        CommentListFrag fragment = new CommentListFrag();
        Bundle args = new Bundle();
        // Pass the deal ID to the fragment
        args.putLong(ARG_DEAL_ID, dealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dealId = getArguments().getLong(ARG_DEAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        // Check if dealId is valid
        if (dealId > 0) {
            // Load and display comments for the deal
            initCommentsRecyclerView(dealId);
        }

        return view;
    }



    // CommentListFrag.java
    private void initCommentsRecyclerView(long dealId) {
        RetrofitService retrofitService = new RetrofitService(requireContext()); // Use 'requireContext()' for Fragments
        DealApi dealApi = retrofitService.getRetrofit().create(DealApi.class);

        Call<List<DealComment>> call = dealApi.getCommentsForDeal(dealId);
        call.enqueue(new Callback<List<DealComment>>() {
            @Override
            public void onResponse(Call<List<DealComment>> call, Response<List<DealComment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DealComment> comments = response.body();
                    // Initialize the RecyclerView
                    recyclerViewComments = requireView().findViewById(R.id.recyclerViewComments); // Use 'requireView()' for Fragments
                    adapter = new CommentsAdapter(comments);
                    recyclerViewComments.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerViewComments.setAdapter(adapter);

                    // After setting the adapter, update the height of the RecyclerView

                } else {
                    // Handle error
                    Toast.makeText(requireContext(), "Failed to get comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DealComment>> call, Throwable t) {
                // Handle failure
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
