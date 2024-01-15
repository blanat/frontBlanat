package com.example.myapplication.UI;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Discussion;
import com.example.myapplication.model.Enum.Categories;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.messaging.FirebaseMessaging;

public class CreateDiscFragment extends Fragment {
    private EditText etDiscussionTitle;
    private EditText etDiscussionDescription;
    private Button btnCreateDiscussion;

    private final String my_channel_id="nassima";
    private Categories selectedCategory;
    private PowerSpinnerView spinnerCategories;

    public CreateDiscFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_discussion, container, false);

        etDiscussionTitle = view.findViewById(R.id.etDiscussionTitle);
        etDiscussionDescription = view.findViewById(R.id.etDiscussionDescription);
        btnCreateDiscussion = view.findViewById(R.id.btnCreateDiscussion);
        spinnerCategories = view.findViewById(R.id.spinnerCategories);

        // Rest of your code remains unchanged...
        List<Categories> categoriesList = Arrays.asList(Categories.values());
        List<String> categoryNames = new ArrayList<>();
        for (Categories category : categoriesList) {
            categoryNames.add(category.name().replace("_", " "));
        }

        spinnerCategories.setItems(categoryNames); // Set items directly to PowerSpinnerView

        spinnerCategories.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int oldIndex, String oldItem, int newIndex, String newItem) {
                String selectedItem = categoryNames.get(newIndex);
                for (Categories category : Categories.values()) {
                    if (category.name().equalsIgnoreCase(selectedItem.replace(" ", "_"))) {
                        selectedCategory = category;
                        break;
                    }
                }
                if (selectedCategory != null) {
                }
            }
        });



        TextView txtBack = view.findViewById(R.id.txtBack);


        txtBack.setOnClickListener(v -> {
            // Handle the click on txtBack
            navigateToDiscussionFragment();
        });

        btnCreateDiscussion.setOnClickListener(v -> {
            String token = retrieveToken();
            String title = etDiscussionTitle.getText().toString();
            String description = etDiscussionDescription.getText().toString();

            if (selectedCategory == null) {
                showSnackbar("Please select a category");
            } else if (title.isEmpty()) {
                etDiscussionTitle.setError("Title is required");
            } else if (description.isEmpty()) {
                etDiscussionDescription.setError("Description is required");
            } else {
                // All fields are filled, proceed to create discussion
                createDiscussion(token, title, description, selectedCategory);
            }
        });






        etDiscussionTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etDiscussionTitle.setError(null);
            }
        });

        etDiscussionDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etDiscussionDescription.setError(null);
            }
        });



        return view;
    }


    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red)); // Set your desired color
        snackbar.show();
    }
    private String retrieveToken() {
        // Retrieve the token from SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jwtToken = preferences.getString("jwtToken", "");

        // Log the token for debugging
        Log.d("Token=====", "Retrieved JWT Token: " + jwtToken);

        // Ensure the token is not empty
        if (jwtToken.isEmpty()) {
            Log.e("Token=====", "Token is empty!");
        }

        return jwtToken;
    }


    private void createDiscussion(String token, String title, String description, Categories selectedCategory) {
        RetrofitService retrofitService = new RetrofitService(requireContext());
        UserApi userApi = retrofitService.getRetrofit().newBuilder()
                .build()
                .create(UserApi.class);

        Discussion discussionDTO = new Discussion();
        discussionDTO.setTitre(title);
        discussionDTO.setDescription(description);
        discussionDTO.setCategories(selectedCategory);

        Call<Discussion> call = userApi.createDiscussion(discussionDTO);
        call.enqueue(new Callback<Discussion>() {
            @Override
            public void onResponse(Call<Discussion> call, Response<Discussion> response) {
                if (response.isSuccessful()) {
                    Discussion createdDiscussion = response.body();

                    if (createdDiscussion != null) {
                        Toast.makeText(requireContext(), "Discussion created successfully", Toast.LENGTH_SHORT).show();


                        Log.d("MyApp", "Before sendNotification");
                        sendNotification("Nouvelle Discussion créée!", "Titre : " + createdDiscussion.getTitre());
                        Log.d("MyApp", "After sendNotification");


                        // Navigate to DiscussionFragment after creating the discussion
                        navigateToDiscussionFragment();

                    } else {
                        Toast.makeText(requireContext(), "Empty response body", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to create discussion. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Discussion> call, Throwable t) {
                Log.e("CreateDiscussionFragment", "Network error: " + t.getMessage());
            }
        });
    }

    private void navigateToDiscussionFragment() {
        // Use FragmentTransaction to replace the current fragment with DiscussionFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, DiscussionFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void sendNotification(String title, String message) {
        // Obtenez le jeton FCM
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String targetDeviceToken = task.getResult();
                        Log.d("CreateDiscussionFragment", "FCM Token: " + targetDeviceToken);

                        // Vérifiez si le jeton FCM est obtenu avec succès
                        if (targetDeviceToken != null && !targetDeviceToken.isEmpty()) {
                            // Construisez la notification
                            // Construisez la notification
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireContext(), my_channel_id)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(title)
                                    .setContentText(message)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH);

                            // Intent pour ouvrir DiscussionFragment
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            intent.putExtra("notification_clicked", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                            notificationBuilder.setContentIntent(pendingIntent);

                            // Affichez la notification

                            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(my_channel_id, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                                notificationManager.createNotificationChannel(channel);
                            }

                            int notificationId = (int) System.currentTimeMillis();
                            notificationManager.notify(notificationId, notificationBuilder.build());

                        } else {
                            // Échec de l'obtention du jeton FCM
                            Log.e("CreateDiscussionFragment", "Failed to get FCM token");
                        }
                    } else {
                        // Échec de l'obtention du jeton FCM
                        Log.e("CreateDiscussionFragment", "Failed to get FCM token", task.getException());
                    }
                });


    }

    private void sendFcmMessage(String fcmMessage) {
        Log.d("FCMMessage", fcmMessage);

    }


}
