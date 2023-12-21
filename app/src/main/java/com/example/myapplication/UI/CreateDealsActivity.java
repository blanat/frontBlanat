package com.example.myapplication.UI;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import okhttp3.RequestBody;







import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import com.example.myapplication.R;
import com.example.myapplication.Services.DealService;
import com.example.myapplication.Services.RealPathUtil;
import com.example.myapplication.UI.Adapters.ImageAdapter;
import com.example.myapplication.model.Deal;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateDealsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;
    private DealService dealService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);

        recyclerView = findViewById(R.id.recyclerView);
        Button selectButton = findViewById(R.id.select);
        Button submitButton = findViewById(R.id.submit);

        // Initialize imagePaths and dealService
        imagePaths = new ArrayList<>();
        dealService = new DealService(this);

        // Initialize RecyclerView
        imageAdapter = new ImageAdapter(this, imagePaths);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageAdapter);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if the app has the necessary permission
                if (ContextCompat.checkSelfPermission(CreateDealsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(CreateDealsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    // Permission is already granted, proceed with your logic
                    openImagePicker();
                }


            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Deal instance with title and description
                EditText titleEditText = findViewById(R.id.TitreDealText);
                EditText descriptionEditText = findViewById(R.id.DescriptionDealText);

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                /*
                ajouter les autres attribut dans le model Deal
                * */
                Deal deal = new Deal(title, description);

                // Assuming you have GsonConverterFactory in your Retrofit setup
                Gson gson = new Gson();
                String dealJson = gson.toJson(deal);
                RequestBody dealRequestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), dealJson);
                dealService.uploadDeal(dealRequestBody, imagePaths, new Callback<RequestBody>() {
                    @Override
                    public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                        Log.d("Retrofit", "onResponse: " + response.toString());

                    }
                    @Override
                    public void onFailure(Call<RequestBody> call, Throwable t) {
                        Log.e("Retrofit", "onFailure: " + t.getMessage());
                        // Handle the failure
                    }

                });
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
                openImagePicker();
            } else {
                // Permission denied, handle accordingly
                // You may show a message or disable functionality that requires this permission
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {
            // Handle the selected images from the intent data
            ArrayList<String> selectedImagePaths = new ArrayList<>();

            ClipData clipData = data.getClipData();
            if (clipData != null) {
                Log.d("clipData", "clipData is not empty" + clipData );

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    Log.d("imageUri", "imageUri: is " + imageUri );

                    String imagePath = getImagePathFromUri(imageUri);
                    Log.d("imagePath", "imagePath: is " + imagePath );

                    selectedImagePaths.add(imagePath);
                }
            } else {
                Log.d("clipDataPROBLEM", "clipDataPROBLEM====================================== "  );
            }

            // Update the imagePaths list and notify the adapter
            imagePaths.addAll(selectedImagePaths);
            for (String imagePath : selectedImagePaths) {
                Log.d("ImagePath", "Selected Image Path: " + imagePath);
            }

            imageAdapter.notifyDataSetChanged();
        }
    }


    private String getImagePathFromUri(Uri uri) {
        String realPath = RealPathUtil.getRealPath(this, uri);

        if (realPath != null) {
            // Log the obtained file path
            Log.d("ImagePath", "Obtained Image Path: " + realPath);
            return realPath;
        } else {
            // Log that the file path is null
            Log.d("ImagePath", "Image Path is null");
            return null;
        }
    }


    private void initializeComponents() {

    }


}
