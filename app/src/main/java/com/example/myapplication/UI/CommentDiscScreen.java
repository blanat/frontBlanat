package com.example.myapplication.UI;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.Adapter.CommentDiscAdapter;
import com.example.myapplication.retrofit.RequestInterceptor;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.MessageDTO;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentDiscScreen extends AppCompatActivity {

    private ListView listViewComments;
    private CommentDiscAdapter commentAdapter;
    private List<MessageDTO> commentsList;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_discussion);

        // Initialize your views and adapters
        listViewComments = findViewById(R.id.listViewComments);
        Button btnAddComment = findViewById(R.id.btnAddComment);
        // Find the new EditText for content
        EditText etContent = findViewById(R.id.etContent);
        commentAdapter = new CommentDiscAdapter(this, R.layout.item_comments_discussion); // Initialize the adapter here
        listViewComments.setAdapter(commentAdapter);

        // Retrieve the ID of the selected discussion (e.g., from an intent or another source)
        Long discussionId = getIntent().getLongExtra("discussionId", -1L);

        // Load comments for the selected discussion
        loadComments(discussionId);

        // Add a click listener to the btnAddComment button
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the content from the etContent EditText
                String content = etContent.getText().toString();
                if (!content.isEmpty()) {
                    // Get the discussion ID from the intent
                    Long discussionId = getIntent().getLongExtra("discussionId", -1L);

                    // Call the backend to add the comment
                    addCommentToDiscussion(discussionId, content);
                    commentAdapter.notifyDataSetChanged();
                    etContent.setText("");

                }
            }
        });
    }

    // Rest of the code remains unchanged
    private boolean isAuthenticated(String token) {
        // Implement your logic to check if the user is authenticated
        // For example, you can check if the token is valid or not expired
        return token != null && !token.isEmpty();
    }
    private String retrieveToken() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }
    private void loadComments(Long discussionId) {
        String token = retrieveToken();
        Log.d("mytoken", "mytoken" + token);

        RetrofitService retrofitService = new RetrofitService(this);
        // Create an instance of RequestInterceptor with the token

        // Pass the interceptor to Retrofit setup
        UserApi apiService = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        Call<List<MessageDTO>> call = apiService.getCommentsByDiscussionId(discussionId);

        call.enqueue(new Callback<List<MessageDTO>>() {
            @Override
            public void onResponse(Call<List<MessageDTO>> call, Response<List<MessageDTO>> response) {
                if (response.isSuccessful()) {
                    commentsList = response.body();
                    commentAdapter.clear(); // Clear existing items in the adapter
                    commentAdapter.addAll(commentsList); // Add new items to the adapter
                    commentAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                } else {
                    // Handle error response
                    Log.e("CommentDiscScreen", "Error response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<MessageDTO>> call, Throwable t) {
                // Handle failure
                Log.e("CommentDiscScreen", "Network error: " + t.getMessage());
            }
        });
    }

    // Vous pouvez également ajouter la méthode addComment pour gérer l'ajout de commentaires




    private void addCommentToDiscussion(Long discussionId, String content) {
        // Retrieve the token from SharedPreferences
        String token = retrieveToken();

        // Retrofit setup
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        // Create a DTO (Data Transfer Object) for the message content
        MessageDTO messageDTO = new MessageDTO(content);

        // Make a POST request to add a comment to the discussion
        Call<MessageDTO> call = userApi.addMessage(discussionId, messageDTO);
        call.enqueue(new Callback<MessageDTO>() {
            @Override
            public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                if (response.isSuccessful()) {
                    // Comment added successfully, refresh the comments list
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadComments(discussionId);
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    // Log additional details about the error
                    Log.e("CommentDiscScreen", "Failed to add comment. Code: " + response.code());
                    Log.e("CommentDiscScreen", "Error body: " + response.errorBody());
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<MessageDTO> call, Throwable t) {
                // Handle the failure
                Log.e("CommentDiscScreen", "Network error: " + t.getMessage());
            }
        });
    }


}
