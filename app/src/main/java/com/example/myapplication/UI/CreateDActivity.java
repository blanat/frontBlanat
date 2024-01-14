package com.example.myapplication.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.model.Enum.Categories;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import okhttp3.RequestBody;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
public class CreateDActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;
    private DealService dealService;

    //===================
    TextView dateselect, dateselectF;
    //EditText   datedebut, datefin;
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
        ImageView submitButton = findViewById(R.id.submit);

        // Initialize imagePaths and dealService
        imagePaths = new ArrayList<>();
        dealService = new DealService(this);

        // Initialize RecyclerView
        imageAdapter = new ImageAdapter(this, imagePaths);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageAdapter);

        // Initialize the ActivityResultLauncher for permission request
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                openImagePicker();
            } else {
                // Handle the case where permission is denied
                // You may show a message or disable functionality that requires this permission
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the ActivityResultLauncher for image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        handleImagePickerResult(result.getData());
                    }
                });
        //============================
        //new:
        TextView backButton = findViewById(R.id.back);

        // Set an OnClickListener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onBackPressed method to navigate back
                BackToDeals();
            }
        });

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


        /*selectButton.setOnClickListener(view -> {
            boolean isReadMediaPermissionGranted = ContextCompat.checkSelfPermission(CreateDActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;

            boolean isReadExternalStoragePermissionGranted = ContextCompat.checkSelfPermission(CreateDActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;

            if (!isReadMediaPermissionGranted && !isReadExternalStoragePermissionGranted) {
                // Log statement to verify that this block is executed
                Log.d("Permission", "Permission not granted, launching permission request");

                // Permission is not granted, request it using the launcher
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                // or you can use permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                // Permission is already granted, proceed with your logic
                openImagePicker();
                imageAdapter.setShowDefaultImages(false);
            }

            imageAdapter.setShowDefaultImages(false);
        });
*/

        selectButton.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(CreateDActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Log statement to verify that this block is executed
                Log.d("Permission", "Permission not granted, launching permission request");

                // Permission is not granted, request it using the launcher
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                // Permission is already granted, proceed with your logic
                openImagePicker();
                imageAdapter.setShowDefaultImages(false);

            }
            imageAdapter.setShowDefaultImages(false);
        });




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    // Create a Deal instance with title and description
                    EditText titleEditText = findViewById(R.id.dealtitle);
                    EditText descriptionEditText = findViewById(R.id.description);
                    EditText lienDealEditText = findViewById(R.id.dealLink);
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


                    Deal deal = new Deal(title, description, lienDeal, price, newPrice, localisation, selectedCategory, deliveryExist, deliveryPrice, debutDateString, finDateString);

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

                    showValidationDialog();

                   /* // Create an intent to navigate to navActivity
                    Intent intent = new Intent(CreateDActivity.this, navActivity.class);

                    // Add extra to specify that you want to show HomeFragment initially
                    intent.putExtra("initial_fragment", "home");

                    // Start the activity
                    startActivity(intent);
*/

                }else{
                    Toast.makeText(CreateDActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showValidationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deal Submission")
                .setMessage("Bravo ! Votre offre a été soumise avec succès. Attendez-vous à une validation prochaine par notre modérateur.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Move to navActivity or any other logic you need
                    startActivity(new Intent(CreateDActivity.this, navActivity.class));
                    finish(); // Optionally finish this activity if you don't need it in the back stack
                })
                .setCancelable(false)
                .show();
    }


    private boolean validateForm() {
        boolean isValid = true;


        //=========Deal Link Validation==========
        // Check if the deal link is provided, not empty, and starts with "http://" or "https://"
        TextInputEditText dealLinkEditText = findViewById(R.id.dealLink);
        String dealLink = dealLinkEditText.getText().toString().trim();
        if ((!dealLink.startsWith("http://") && !dealLink.startsWith("https://"))) {
            dealLinkEditText.setError(getString(R.string.enter_deal_link));
            isValid = false;
        }

        //=========Deal Title Validation==========
        // Check if the deal title is provided and not empty
        TextInputEditText dealTitleEditText = findViewById(R.id.dealtitle);
        if (TextUtils.isEmpty(dealTitleEditText.getText())) {
            dealTitleEditText.setError(getString(R.string.msg_veuillez_entrer));
            isValid = false;
        }

        //=========Deal Title Validation==========
        // Check if the deal title is provided and not empty
        TextInputEditText dealDescriptionEditText = findViewById(R.id.description);
        if (TextUtils.isEmpty(dealDescriptionEditText.getText())) {
            dealDescriptionEditText.setError(getString(R.string.msg_veuillez_entrer2));
            isValid = false;
        }


        //=========Deal New Price Validation==========
        // Check if "Nouveau Prix DH" field is empty
        EditText prixNEditText = findViewById(R.id.PrixN);
        String priceNValue = prixNEditText.getText().toString().trim();
        if (priceNValue.isEmpty()) {
            prixNEditText.setError(getString(R.string.msg_veuillez_entrer4));
            isValid = false;
        }


        //=========Deal New Price Validation==========
        // Check if "Nouveau Prix DH" field is empty
        EditText prixAEditText = findViewById(R.id.PrixA);
        String priceAValue = prixNEditText.getText().toString().trim();
        if (priceAValue.isEmpty()) {
            prixAEditText.setError(getString(R.string.msg_veuillez_entrer5));
            isValid = false;
        }



        //=========Deal Localisation Validation==========
        // Check if the deal title is provided and not empty
        TextInputEditText dealLocalisationEditText = findViewById(R.id.localisation);
        if (TextUtils.isEmpty(dealLocalisationEditText.getText())) {
            dealLocalisationEditText.setError(getString(R.string.msg_veuillez_entrer3));
            isValid = false;
        }


        //=======checkbox and delivery checker===========
        // Check if at least one checkbox is selected
        RadioGroup radioGroup = findViewById(R.id.segmentedControl);
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            // No checkbox selected, show error
            TextView errorTextView = findViewById(R.id.checkboxValidation); // Replace with the actual ID
            errorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        }
        // Check if delivery is selected as "Yes" and price is provided
        RadioButton deliveryYesRadioButton = findViewById(R.id.option1);
        if (deliveryYesRadioButton.isChecked()) {
            EditText prixLivraisonEditText = findViewById(R.id.prixlivraison);
            if (TextUtils.isEmpty(prixLivraisonEditText.getText())) {
                prixLivraisonEditText.setError("Please enter the delivery price");
                isValid = false;
            }
        }

        //=================Images Validation======================
        // Check if at least one non-default image is selected
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ImageAdapter adapter = (ImageAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            List<String> imagePaths = adapter.getImagePaths();

            boolean isAtLeastOneNonDefaultImageSelected = false;

            for (String imagePath : imagePaths) {
                // Assuming "defaultImagePath" is the value indicating a default image
                if (!imagePath.equals("defaultImagePath")) {
                    isAtLeastOneNonDefaultImageSelected = true;
                    break;
                }
            }

            TextView imageValidationTextView = findViewById(R.id.imageValidation); // Replace with the actual ID
            if (!isAtLeastOneNonDefaultImageSelected) {
                // No non-default image selected, show error
                imageValidationTextView.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                // At least one non-default image selected, hide the validation message
                imageValidationTextView.setVisibility(View.GONE);
            }
        }

        //=================Date Debut Validation======================
        TextView dateDebutTextView = findViewById(R.id.datedebut);
        TextView dateValidationTextViewD = findViewById(R.id.dateValidationD);

        String dateDebut = dateDebutTextView.getText().toString().trim();

        if (TextUtils.isEmpty(dateDebut) || dateDebut.equals("Date de Debut *")) {
            // Date de Debut is not provided or still has the default value, show error
            dateValidationTextViewD.setVisibility(View.VISIBLE);
            dateValidationTextViewD.setText("La date de Debut est requise");
            isValid = false;
        } else {
            // Date de Debut is provided, hide the validation message
            dateValidationTextViewD.setVisibility(View.GONE);
        }

        //=================Date Fin Validation======================
        TextView dateFinTextView = findViewById(R.id.datefin);
        TextView dateValidationTextViewF = findViewById(R.id.dateValidationF);

        String dateFin = dateFinTextView.getText().toString().trim();

        if (TextUtils.isEmpty(dateFin) || dateFin.equals("Date de fin *")) {
            // Date de fin is not provided or still has the default value, show error
            dateValidationTextViewF.setVisibility(View.VISIBLE);
            dateValidationTextViewF.setText("La date de fin est requise");
            isValid = false;
        } else {
            // Date de fin is provided, hide the validation message
            dateValidationTextViewF.setVisibility(View.GONE);
        }


        //=============Validation for category================
        // Validation for category
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.categories);
        String selectedCategory = autoCompleteTextView.getText().toString().trim();

        if (TextUtils.isEmpty(selectedCategory) || selectedCategory.equals("catégorie")) {
            autoCompleteTextView.setError("La catégorie est requise");
            isValid = false;
        } else {
            autoCompleteTextView.setError(null);
        }



        return isValid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE ) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
                openImagePicker();
            } else {
                // Permission denied, handle accordingly
                // You may show a message or disable functionality that requires this permission
                Log.e("Permission", "Permission denied");
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }





    private void openImagePicker() {
        Log.d("CreateDActivity", "openImagePicker: Starting image picker activity");

        // Use Intent.ACTION_OPEN_DOCUMENT for document access on Android 11 and above
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // Use the ActivityResultLauncher to launch the activity
        imagePickerLauncher.launch(intent);
        imageAdapter.setShowDefaultImages(false);
    }


    private void handleImagePickerResult(Intent data) {
        Log.d("CreateDActivity", "handleImagePickerResult: Handling image picker result");
        if (data == null) {
            Log.d("CreateDActivity", "handleImagePickerResult: Intent data is null");
            return;
        }

        // Handle the selected images from the intent data
        ArrayList<String> selectedImagePaths = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            Log.d("CreateDActivity", "handleImagePickerResult: clipData is not empty" + clipData);

            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri imageUri = clipData.getItemAt(i).getUri();
                Log.d("CreateDActivity", "handleImagePickerResult: imageUri: is " + imageUri);

                String imagePath = getImagePathFromUri(imageUri);
                Log.d("CreateDActivity", "handleImagePickerResult: imagePath: is " + imagePath);

                selectedImagePaths.add(imagePath);
            }
        } else {
            // Handle the case where a single image is selected without ClipData
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String imagePath = getImagePathFromUri(imageUri);
                selectedImagePaths.add(imagePath);
            }
        }

        // Update the imagePaths list and notify the adapter
        imagePaths.addAll(selectedImagePaths);
        for (String imagePath : selectedImagePaths) {
            Log.d("CreateDActivity", "handleImagePickerResult: Selected Image Path: " + imagePath);
        }

        imageAdapter.notifyDataSetChanged();
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


    /*@Override
    public void onBackPressed() {
        // Check if the fragment manager has fragments in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Pop the back stack to navigate back
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments in the back stack, finish the activity
            super.onBackPressed();
        }
    }*/

    public void BackToDeals() {
        // Check if the fragment manager has fragments in the back stack
        // Create an intent to navigate to navActivity
        Intent intent = new Intent(CreateDActivity.this, navActivity.class);

        // Add extra to specify that you want to show HomeFragment initially
        intent.putExtra("initial_fragment", "home");

        // Start the activity
        startActivity(intent);
    }





}