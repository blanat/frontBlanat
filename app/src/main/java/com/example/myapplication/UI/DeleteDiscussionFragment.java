package com.example.myapplication.UI;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.UI.Adapters.DeleteDiscussionAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.Discussion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteDiscussionFragment extends Fragment {
    private RetrofitService retrofitService;
    private UserApi userApi;
    private DeleteDiscussionAdapter discussionAdapter;
    private Button btnShowDialog;
    private Button btnDelete;
    private List<Discussion> userDiscussionItemList = new ArrayList<>();

    public DeleteDiscussionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_mydiscussions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RetrofitService and UserApi only once
        retrofitService = new RetrofitService(requireContext());
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        // Retrieve the token from SharedPreferences
        String token = retrieveToken();
        Log.d("UserDiscussionsFragment", "Token: " + token);

        // Check if the user is authenticated
        if (isAuthenticated(token)) {
            // User is authenticated, proceed to fetch user discussions
            fetchUserDiscussions(token);
        } else {
            // User is not authenticated, handle accordingly (e.g., show the login screen)
            Log.e("UserDiscussionsFragment", "User not authenticated");
            // Add your logic here, such as showing a login screen or redirecting to the login activity.
        }

        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDiscussionFragment();
            }
        });
    }
    private void navigateToDiscussionFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DiscussionFragment())
                .addToBackStack(null)
                .commit();
    }





    private void showDialog(final Long discussionIdToDelete) {
        // Use the Fragment's context to create the Dialog
        Dialog dialog = new Dialog(requireContext(), R.style.DialogStyle);
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
                // Call the method to delete the discussion
                deleteDiscussion(discussionIdToDelete);

                // Dismiss the dialog after deletion
                dialog.dismiss();
            }
        });

        Button btn_no = dialog.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void fetchUserDiscussions(String token) {
        // Utilisez Retrofit pour récupérer les discussions créées par l'utilisateur
        RetrofitService retrofitService = new RetrofitService(requireContext());
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
        // Retrieve the token from SharedPreferences using the Fragment's context
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");
        // Log the token for debugging
        Log.d("Token", "Retrieved JWT Token: " + jwtToken);
        return jwtToken;
    }

    // Utilisez la méthode updateUI pour mettre à jour l'interface utilisateur avec les discussions de l'utilisateur
    private void updateUI(List<Discussion> userDiscussionItemList) {

        ListView listView = requireView().findViewById(R.id.listView);
        discussionAdapter = new DeleteDiscussionAdapter(requireContext(), userDiscussionItemList);
        listView.setAdapter(discussionAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the click on the discussion item
            Discussion selectedDiscussion = userDiscussionItemList.get(position);

            // Show the dialog when a discussion item is clicked, passing the discussion ID
            showDialog(selectedDiscussion.getId());

            // Additional logic if needed based on the selected discussion
            // For example, you can pass the selected discussion to the dialog or perform other actions.
        });

        // Log to check if the method is called
        Log.d("UserDiscussionsActivity", "Method updateUI called");
    }

    private void deleteDiscussion(Long discussionId) {
        Call<Void> deleteCall = userApi.deleteDiscussionAndMessages(discussionId);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Discussion supprimée avec succès
                    Log.d("DeleteDiscussionFragment", "Discussion supprimée avec succès");
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
                Log.e("DeleteDiscussionFragment", "Erreur réseau lors de la suppression de la discussion : " + t.getMessage());
                // Vous pouvez afficher un message à l'utilisateur ici s'il y a une erreur réseau
            }
        });
    }

    private void handleDeleteDiscussionFailure(Response<Void> response) {
        // Gestion des erreurs lors de la suppression de la discussion
        Log.e("DeleteDiscussionFragment", "Échec de la suppression de la discussion. Code : " + response.code());
        try {
            Log.e("DeleteDiscussionFragment", "Corps de l'erreur : " + response.errorBody().string());
            // Vous pouvez afficher un message d'erreur à l'utilisateur ici en utilisant une notification ou un Toast
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



