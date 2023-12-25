package com.example.myapplication.UI.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.UI.details.CommentListFrag;
import com.example.myapplication.UI.details.DealDetailsFrag;
import com.example.myapplication.model.CommentRequest;
import com.example.myapplication.retrofit.DealApi;
import com.example.myapplication.R;
import com.example.myapplication.model.listData;
import com.example.myapplication.retrofit.RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsDealActivity extends AppCompatActivity {

    private DealApi DealApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_deal);

        // Create the RetrofitService instance
        RetrofitService retrofitService = new RetrofitService(this);

        // Create the DealService instance
        DealApi = retrofitService.getRetrofit().create(DealApi.class);

        // Receive Intent data
        Intent intent = getIntent();
        listData deal = intent.getParcelableExtra("deal");

        if (deal != null) {
            // Initialize and set up the top fragment with deal data
            DealDetailsFrag dealDetailsFrag = DealDetailsFrag.newInstance(deal);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerTop, dealDetailsFrag)
                    .commit();

            // Initialize and set up the bottom fragment for comments
            CommentListFrag commentListFrag = CommentListFrag.newInstance(deal.getDealID());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerBottom, commentListFrag)
                    .commit();

            // Set up the OnClickListener for the "Send" button
            Button buttonSendComment = findViewById(R.id.buttonSendComment);
            final EditText editTextComment = findViewById(R.id.editTextComment);

            buttonSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentText = editTextComment.getText().toString().trim();

                    if (!commentText.isEmpty()) {
                        CommentRequest commentRequest = new CommentRequest(deal.getDealID(), commentText);

                        Call<ResponseBody> call = DealApi.addComment(commentRequest);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(DetailsDealActivity.this, "Comment added: " + commentText, Toast.LENGTH_SHORT).show();

                                    // Refresh the comment list
                                    CommentListFrag commentListFrag = (CommentListFrag) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerBottom);
                                    if (commentListFrag != null) {
                                        commentListFrag.refreshComments();
                                    }
                                } else {
                                    Toast.makeText(DetailsDealActivity.this, "Error adding comment", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(DetailsDealActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(DetailsDealActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
