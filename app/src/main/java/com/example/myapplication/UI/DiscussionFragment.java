package com.example.myapplication.UI;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.UI.Adapters.DiscussionAdapter;
import com.example.myapplication.UI.DeleteDiscussionActivity;

import com.example.myapplication.model.Discussion;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class DiscussionFragment extends Fragment {

    private DiscussionAdapter discussionAdapter;
    private List<Discussion> discussionItemList = new ArrayList<>();

    public DiscussionFragment() {
        // Required empty public constructor
    }

    public static DiscussionFragment newInstance() {
        return new DiscussionFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        // Setup ListView and adapter
        ListView listView = view.findViewById(R.id.listView);
        discussionAdapter = new DiscussionAdapter(requireContext(), discussionItemList);
        listView.setAdapter(discussionAdapter);

        // Retrieve the token from SharedPreferences
        String token = retrieveToken();

        // Check if the user is authenticated
        if (isAuthenticated(token)) {
            // User is authenticated, proceed to fetch discussions
            fetchDiscussions(token);
        } else {
            // User is not authenticated, handle accordingly (e.g., show login screen)
            Log.e("DiscussionFragment", "User not authenticated");
            // Add your logic here, such as showing a login screen or redirecting to the login activity.
        }

        ImageView imageFloatingIcon = view.findViewById(R.id.imageFloatingicon);


        // Add a click listener to imageFloatingIcon
        imageFloatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the discussion creation page
                // Start CreateDiscScreen using startActivityForResult with the contract
                Intent intent = new Intent(requireContext(), CreateDiscScreen.class);
                Log.d("DiscussionFragment", "Launching createDiscLauncher");
                createDiscLauncher.launch(intent);
                Log.d("DiscussionFragment", "createDiscLauncher launched successfully");
            }
        });


        ImageView deleteDiscussionImageView = view.findViewById(R.id.deletediscussion);

        deleteDiscussionImageView.setOnClickListener(v -> {
            // Déclencher l'intention de passer à l'activité de liste de discussions créées par l'utilisateur
            Intent intent = new Intent(requireContext(),DeleteDiscussionActivity.class);
            startActivity(intent);
        });

        return view;
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
                            discussionAdapter =new DiscussionAdapter(requireContext(), discussionItemList);
                            // Find the ListView in your layout
                            ListView listView = getView().findViewById(R.id.listView);
                            listView.setAdapter(discussionAdapter);
                        }
                    }
                }
            }
    );


    private void fetchDiscussions(String token) {
        // Retrofit setup
        RetrofitService retrofitService = new RetrofitService(requireContext());

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
        //SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    private void updateUI(List<Discussion> discussionItemList) {
        // Create a custom adapter
        DiscussionAdapter adapter = new DiscussionAdapter(requireContext(), discussionItemList);
        // Find the ListView in your layout
        ListView listView = getView().findViewById(R.id.listView);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set click listener on each item in the list
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle item click, and call the backend endpoint to update views
            Discussion selectedDiscussion = discussionItemList.get(position);
            Log.d("discussionactuel", "ddd" + selectedDiscussion.getId());
            updateDiscussionViews(selectedDiscussion.getId());

            // Start CommentDiscussionActivity and pass discussionId as an extra
            //Intent intent = new Intent(DiscussionFragment.this, CommentDiscScreen.class);
            Intent intent = new Intent(requireContext(), CommentDiscScreen.class);

            intent.putExtra("discussionId", selectedDiscussion.getId());
            startActivity(intent);
        });

        // Log statement to check if the method is called
        Log.d("DiscussionScreen", "updateUI method called");
    }


    private void updateDiscussionViews(Long discussionId) {
        String token = retrieveToken(); // Assurez-vous de récupérer le token d'authentification

        RetrofitService retrofitService = new RetrofitService(requireContext());

        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        Call<Long> call = userApi.updateViews(discussionId, "Bearer " + token);
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
    public void onResume() {
        super.onResume();
        String token = retrieveToken();

        if (isAuthenticated(token)) {
            fetchDiscussions(token);
        } else {
            Log.e("DiscussionFragment", "User not authenticated");
            // Handle unauthenticated user
        }
    }


}