package com.example.myapplication.UI;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.myapplication.model.Enum.Categories;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Services.DealService;
import com.example.myapplication.Services.RealPathUtil;
import com.example.myapplication.UI.Adapters.ImageAdapter;
import com.example.myapplication.model.Deal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateDActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;
    private DealService dealService;

    //===================
    TextView dateselect, dateselectF;
    EditText   datedebut, datefin;
    DatePickerDialog.OnDateSetListener setListener;
    Categories[] items = Categories.values();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;


    //===================
    private boolean deliveryExist;
    private float deliveryPrice = 0; // Default value for cases when "No" is selected
    //===================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_deals);

        //=====================================
        dateselect = findViewById(R.id.datedebut);
        //datedebut = findViewById(R.id.datededebut);
        dateselectF = findViewById(R.id.datefin);
        //datefin = findViewById(R.id.datedefin);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        //=====================================

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



        //============================
        //new:

        //=============================
        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateDActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        dateselect.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        dateselectF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateDActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        dateselectF.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Categorie logique
        autoCompleteTextView = findViewById(R.id.categories);

        // Convert Categories enum values to a String array
        String[] items = new String[Categories.values().length];
        for (int i = 0; i < Categories.values().length; i++) {
            items[i] = Categories.values()[i].toString();
        }

        // Initialize ArrayAdapter with the modified items array
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, items);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                // Retrieve the selected Categories enum value
                Categories selectedCategory = Categories.values()[i];
                Toast.makeText(CreateDActivity.this, "Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }
        });



        //================================
        // Checkbox logic
        RadioGroup radioGroup = findViewById(R.id.segmentedControl);
        EditText prixLivraisonEditText = findViewById(R.id.prixlivraison);

        // Set a listener for the RadioGroup to handle checkbox changes
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleCheckboxChange(checkedId, prixLivraisonEditText);
            }
        });

        //================================

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if the app has the necessary permission
                if (ContextCompat.checkSelfPermission(CreateDActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(CreateDActivity.this,
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
                EditText titleEditText = findViewById(R.id.dealtitle);
                EditText descriptionEditText = findViewById(R.id.description);
                EditText lienDealEditText = findViewById(R.id.line);
                EditText priceEditText = findViewById(R.id.PrixN);
                EditText newPriceEditText = findViewById(R.id.PrixA);
                EditText localisationEditText = findViewById(R.id.localisation);
                AutoCompleteTextView categoryAutoComplete = findViewById(R.id.categories);


                //===================

                // Retrieve selected RadioButton ID from the RadioGroup
                RadioGroup radioGroup = findViewById(R.id.segmentedControl);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // Retrieve the EditText for delivery price
                EditText prixLivraisonEditText = findViewById(R.id.prixlivraison);

                //===================
                // Retrieve the selected date values
                String debutDateString = dateselect.getText().toString();
                String finDateString = dateselectF.getText().toString();


                //===================


                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String lienDeal = lienDealEditText.getText().toString();
                float price = Float.parseFloat(priceEditText.getText().toString());
                float newPrice = Float.parseFloat(newPriceEditText.getText().toString());
                String localisation = localisationEditText.getText().toString();
                Categories selectedCategory = Categories.valueOf(categoryAutoComplete.getText().toString());

                // Use the handleCheckboxChange method to update deliveryExist and deliveryPrice
                handleCheckboxChange(selectedRadioButtonId, prixLivraisonEditText);



                Deal deal = new Deal(title, description, lienDeal, price, newPrice, localisation, selectedCategory, deliveryExist, deliveryPrice,debutDateString,finDateString);

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


    // Method to handle changes in the checkbox state
    private void handleCheckboxChange(int selectedRadioButtonId, EditText prixLivraisonEditText) {
        if (selectedRadioButtonId == R.id.option1) {
            deliveryExist = true; // Yes is selected
            // Set visibility to VISIBLE
            prixLivraisonEditText.setVisibility(View.VISIBLE);
            // Retrieve the delivery price from EditText
            String deliveryPriceString = prixLivraisonEditText.getText().toString();
            if (!TextUtils.isEmpty(deliveryPriceString)) {
                deliveryPrice = Float.parseFloat(deliveryPriceString);
            }
        } else if (selectedRadioButtonId == R.id.option2) {
            // Set visibility to GONE
            prixLivraisonEditText.setVisibility(View.GONE);
            deliveryExist = false; // No is selected
        } else {
            // Handle the case where no option is selected
            // You may add additional logic or show a message here
        }
    }




}
