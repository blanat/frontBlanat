package com.example.myapplication.UI;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.Adapter.DiscussionAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.Discussion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionScreen extends AppCompatActivity {
    private DiscussionAdapter discussionAdapter;
    private List<Discussion> discussionItemList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // Retrieve the token from SharedPreferences
        String token = retrieveToken();

        // Check if the user is authenticated
        if (isAuthenticated(token)) {
            // User is authenticated, proceed to fetch discussions
            fetchDiscussions(token);
        } else {
            // User is not authenticated, handle accordingly (e.g., show login screen)
            Log.e("DiscussionScreen", "User not authenticated");
            // Add your logic here, such as showing a login screen or redirecting to the login activity.
        }


        ImageView imageFloatingicon = findViewById(R.id.imageFloatingicon);

        // Ajoutez un écouteur de clic à l'imageFloatingicon
        imageFloatingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigez vers la page de création de discussion
                // Start CreateDiscScreen using startActivityForResult with the contract
                Intent intent = new Intent(DiscussionScreen.this, CreateDiscScreen.class);
                Log.d("DiscussionScreen", "Launching createDiscLauncher");
                createDiscLauncher.launch(intent);
                Log.d("DiscussionScreen", "createDiscLauncher launched successfully");


            }
        });


    }

    // Use ActivityResultContracts.StartActivityForResult to handle the result
    ActivityResultLauncher<Intent> createDiscLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Récupérez la nouvelle discussion créée à partir des données renvoyées
                    Discussion newDiscussion = result.getData().getParcelableExtra("discussion");
                    Log.d("newdisc", "newdisc" + newDiscussion.getCreateurUsername());
                    Log.d("newdisc", "newdisc" + newDiscussion.getTitre());
                    Log.d("newdisc", "data" + result.getData());

                    if (newDiscussion != null) {
                        // Ajouter la nouvelle discussion à la liste existante
                        discussionItemList.add(newDiscussion);

                        // Rafraîchir la liste des discussions dans l'adaptateur
                        if (discussionAdapter != null) {
                            discussionAdapter.notifyDataSetChanged();
                        } else {
                            // Si l'adaptateur n'a pas encore été initialisé, initialisez-le et définissez-le pour la ListView
                            discussionAdapter = new DiscussionAdapter(this, discussionItemList);
                            // Find the ListView in your layout
                            ListView listView = findViewById(R.id.listView);
                            listView.setAdapter(discussionAdapter);
                        }
                    }
                }
            }
    );


    private void fetchDiscussions(String token) {
        // Retrofit setup
        RetrofitService retrofitService = new RetrofitService(this);

        // Create an instance of RequestInterceptor with the token

        // Pass the interceptor to Retrofit setup
        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        // Make a GET request to getAllDiscussions endpoint
        Call<List<Discussion>> call = userApi.getAllDiscussionsInfo();
        call.enqueue(new Callback<List<Discussion>>() {
            @Override
            public void onResponse(Call<List<Discussion>> call, Response<List<Discussion>> response) {
                if (response.isSuccessful()) {
                    // Discussions fetched successfully, update UI
                    discussionItemList = response.body();
                    updateUI(discussionItemList);
                } else {
                    // Log details about the failure
                    Log.e("DiscussionScreen", "Failed to fetch discussions. Code: " + response.code());
                    try {
                        Log.e("DiscussionScreen", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Discussion>> call, Throwable t) {
                // Handle the failure
                Log.e("DiscussionScreen", "Network error: " + t.getMessage());
            }
        });
    }


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

    private void updateUI(List<Discussion> discussionItemList) {
        // Create a custom adapter
        DiscussionAdapter adapter = new DiscussionAdapter(this, discussionItemList);

        // Find the ListView in your layout
        ListView listView = findViewById(R.id.listView);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set click listener on each item in the list
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click, and call the backend endpoint to update views
            Discussion selectedDiscussion = discussionItemList.get(position);
            Log.d("discussionactuel", "ddd" + selectedDiscussion.getId());
            updateDiscussionViews(selectedDiscussion.getId());

            // Start CommentDiscussionActivity and pass discussionId as an extra
            Intent intent = new Intent(DiscussionScreen.this, CommentDiscScreen.class);
            intent.putExtra("discussionId", selectedDiscussion.getId());
            startActivity(intent);
        });

        // Log statement to check if the method is called
        Log.d("DiscussionScreen", "updateUI method called");
    }


    private void updateDiscussionViews(Long discussionId) {
        String token = retrieveToken(); // Assurez-vous de récupérer le token d'authentification

        RetrofitService retrofitService = new RetrofitService(this);

        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        Call<Long> call = userApi.updateViews(discussionId,"Bearer "+token);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Long newViews = response.body();
                    Log.d("DiscussionScreen", "Views updated: " + newViews);
                    // Mettez à jour votre interface utilisateur ou effectuez toute autre action nécessaire avec les nouvelles vues
                } else {
                    // Gérez ici les erreurs si la requête n'a pas abouti (ex : HttpStatus.BAD_REQUEST, HttpStatus.UNAUTHORIZED, etc.)
                    // Vous pouvez vérifier le code de réponse pour savoir quelle erreur s'est produite.
                    Log.e("DiscussionScreen", "Failed to update views. Code: " + response.code());
                    try {
                        Log.e("DiscussionScreen", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // Gérer les erreurs de connexion réseau
                Log.e("DiscussionScreen", "Network error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = retrieveToken();

        if (isAuthenticated(token)) {
            fetchDiscussions(token);
        } else {
            Log.e("DiscussionScreen", "User not authenticated");
            // Handle unauthenticated user
        }
    }


}

