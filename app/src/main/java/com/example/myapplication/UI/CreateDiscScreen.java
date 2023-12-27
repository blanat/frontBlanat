package com.example.myapplication.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Discussion;
import com.example.myapplication.model.Enum.Categories;
import com.example.myapplication.retrofit.RetrofitService;
import com.example.myapplication.retrofit.UserApi;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CreateDiscScreen extends AppCompatActivity {
    private EditText etDiscussionTitle;
    private EditText etDiscussionDescription;
    private Button btnCreateDiscussion;

    private Categories selectedCategory;
    private PowerSpinnerView spinnerCategories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_discussion);

        etDiscussionTitle = findViewById(R.id.etDiscussionTitle);
        etDiscussionDescription = findViewById(R.id.etDiscussionDescription);
        btnCreateDiscussion = findViewById(R.id.btnCreateDiscussion);
        spinnerCategories = findViewById(R.id.spinnerCategories);

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
                    Toast.makeText(CreateDiscScreen.this, "Selected Category: " + selectedCategory.name(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreateDiscussion.setOnClickListener(view -> {
            // Your logic to create the discussion using selectedCategory
            String token = retrieveToken();
            Log.d("CreateDiscScreen", "Selected Category: " + token);

            String title = etDiscussionTitle.getText().toString();
            String description = etDiscussionDescription.getText().toString();

            if (selectedCategory != null) {
                createDiscussion(token, title, description, selectedCategory);
            } else {
                Toast.makeText(CreateDiscScreen.this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String retrieveToken() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    private void createDiscussion(String token, String title, String description, Categories selectedCategory) {
        RetrofitService retrofitService = new RetrofitService(this);
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

                    // Vérification si la réponse contient des données valides
                    if (createdDiscussion != null) {
                        Toast.makeText(CreateDiscScreen.this, "Discussion created successfully", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("discussion", createdDiscussion);
                        setResult(RESULT_OK, resultIntent);

                        // Démarrer l'activité DiscussionScreen pour voir la discussion créée
                        Intent intent = new Intent(CreateDiscScreen.this, DiscussionScreen.class);
                        intent.putExtra("discussion", createdDiscussion);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(CreateDiscScreen.this, "Empty response body", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                } else {
                    Toast.makeText(CreateDiscScreen.this, "Failed to create discussion. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }


            @Override
            public void onFailure(Call<Discussion> call, Throwable t) {
                Log.e("CreateDiscussionActivity", "Network error: " + t.getMessage());
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
