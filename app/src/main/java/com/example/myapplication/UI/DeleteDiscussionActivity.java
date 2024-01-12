package com.example.myapplication.UI;

import android.app.Dialog;
import android.content.Context;
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
    private DiscussionAdapter discussionAdapter;
    private Button btnShowDialog;
    private List<Discussion> userDiscussionItemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydiscussions); // Utilisez le même XML que pour l'activité principale

        // Récupérez le token de SharedPreferences
        String token = retrieveToken();
        Log.d("UserDiscussionsActivity", "Token: " + token);

        // Vérifiez si l'utilisateur est authentifié
        if (isAuthenticated(token)) {
            // Utilisateur authentifié, procédez à la récupération des discussions créées par l'utilisateur
            fetchUserDiscussions(token);
        } else {
            // Utilisateur non authentifié, gérez en conséquence (par exemple, affichez l'écran de connexion)
            Log.e("UserDiscussionsActivity", "Utilisateur non authentifié");
            // Ajoutez votre logique ici, comme afficher un écran de connexion ou rediriger vers l'activité de connexion.
        }

        btnShowDialog = findViewById(R.id.imageFloatingicon);

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }
    private void showDialog(){
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
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }

    // Utilisez la méthode updateUI pour mettre à jour l'interface utilisateur avec les discussions de l'utilisateur
    private void updateUI(List<Discussion> userDiscussionItemList) {
        // Utilisez un adaptateur personnalisé
        discussionAdapter = new DiscussionAdapter(this, userDiscussionItemList);

        // Trouvez la ListView dans votre mise en page
        ListView listView = findViewById(R.id.listView);

        // Définissez l'adaptateur pour la ListView
        listView.setAdapter(discussionAdapter);

        // Définissez un auditeur de clic sur chaque élément de la liste
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Gérez le clic sur l'élément, par exemple, affichez plus de détails sur la discussion
            Discussion selectedDiscussion = userDiscussionItemList.get(position);
            // Faites ce que vous devez faire avec la discussion sélectionnée
            // ...
        });

        // Log pour vérifier si la méthode est appelée
        Log.d("UserDiscussionsActivity", "Méthode updateUI appelée");
    }
}



