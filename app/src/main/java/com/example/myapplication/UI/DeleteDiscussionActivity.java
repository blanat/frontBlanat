package com.example.myapplication.UI;

import android.app.Dialog;
import android.util.Log;

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

import com.example.myapplication.UI.Adapters.DeleteDiscussionAdapter;
import com.example.myapplication.UI.Adapters.DiscussionAdapter;
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

public class DeleteDiscussionActivity extends AppCompatActivity {
    private RetrofitService retrofitService;
    private UserApi userApi;
    private DeleteDiscussionAdapter discussionAdapter;
    private Button btnShowDialog;
    private Button btnDelete;
    private List<Discussion> userDiscussionItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydiscussions); // Utilisez le même XML que pour l'activité principale

        // Initialisez RetrofitService et UserApi une seule fois
        retrofitService = new RetrofitService(this);
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        // Récupérez le token de SharedPreferences
        String token = retrieveToken();

        // Vérifiez si l'utilisateur est authentifié
        if (isAuthenticated(token)) {
            // Utilisateur authentifié, procédez à la récupération des discussions créées par l'utilisateur
            fetchUserDiscussions(token);
        } else {
            // Utilisateur non authentifié, gérez en conséquence (par exemple, affichez l'écran de connexion)
            Log.e("UserDiscussionsActivity", "Utilisateur non authentifié");
            // Ajoutez votre logique ici, comme afficher un écran de connexion ou rediriger vers l'activité de connexion.
        }



    }

    private void showDialog(final Long discussionIdToDelete) {

        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.layout_discussion_dialog);



        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btnDelete = dialog.findViewById(R.id.btn_yes);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Appeler la méthode pour supprimer la discussion
                deleteDiscussion(discussionIdToDelete);

                // Dismiss the dialog after deletion
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void fetchUserDiscussions(String token) {
        // Utilisez Retrofit pour récupérer les discussions créées par l'utilisateur
        RetrofitService retrofitService = new RetrofitService(this);
        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        Call<List<Discussion>> call = userApi.getDiscussionsCreatedByCurrentUser();
        call.enqueue(new Callback<List<Discussion>>() {
            @Override
            public void onResponse(Call<List<Discussion>> call, Response<List<Discussion>> response) {
                if (response.isSuccessful()) {
                    // Discussions récupérées avec succès, mettez à jour l'interface utilisateur
                    userDiscussionItemList = response.body();
                    updateUI(userDiscussionItemList);
                } else {
                    // Log des détails sur l'échec
                    Log.e("UserDiscussionsActivity", "Échec de la récupération des discussions. Code : " + response.code());
                    try {
                        Log.e("UserDiscussionsActivity", "Corps de l'erreur : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Discussion>> call, Throwable t) {
                // Gérez l'échec
                Log.e("UserDiscussionsActivity", "Erreur réseau : " + t.getMessage());
            }
        });
    }

    // ... (rest of the code for updateUI, isAuthenticated, retrieveToken, etc.)

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

    // Utilisez la méthode updateUI pour mettre à jour l'interface utilisateur avec les discussions de l'utilisateur
    private void updateUI(List<Discussion> userDiscussionItemList) {
        discussionAdapter = new DeleteDiscussionAdapter(this, userDiscussionItemList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(discussionAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the click on the discussion item
            Discussion selectedDiscussion = userDiscussionItemList.get(position);

            // Show the dialog when a discussion item is clicked, passing the discussion ID
            showDialog(selectedDiscussion.getId());

            // Additional logic if needed based on the selected discussion
            // For example, you can pass the selected discussion to the dialog or perform other actions.
        });

        // Log pour vérifier si la méthode est appelée
        Log.d("UserDiscussionsActivity", "Méthode updateUI appelée");
    }

    private void deleteDiscussion(Long discussionId) {
        Call<Void> deleteCall = userApi.deleteDiscussionAndMessages(discussionId);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Discussion supprimée avec succès
                    Log.d("DeleteDiscussionActivity", "Discussion supprimée avec succès");
                    // Actualiser la liste des discussions après la suppression
                    fetchUserDiscussions(retrieveToken());
                } else {
                    // Gestion des erreurs lors de la suppression
                    handleDeleteDiscussionFailure(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Gestion des erreurs réseau
                Log.e("DeleteDiscussionActivity", "Erreur réseau lors de la suppression de la discussion : " + t.getMessage());
                // Vous pouvez afficher un message à l'utilisateur ici s'il y a une erreur réseau
            }
        });
    }

    private void handleDeleteDiscussionFailure(Response<Void> response) {
        // Gestion des erreurs lors de la suppression de la discussion
        Log.e("DeleteDiscussionActivity", "Échec de la suppression de la discussion. Code : " + response.code());
        try {
            Log.e("DeleteDiscussionActivity", "Corps de l'erreur : " + response.errorBody().string());
            // Vous pouvez afficher un message d'erreur à l'utilisateur ici en utilisant une notification ou un Toast
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



