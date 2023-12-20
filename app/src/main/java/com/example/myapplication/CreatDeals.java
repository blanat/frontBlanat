package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Services.DealService;
import com.example.myapplication.UI.Adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreatDeals extends AppCompatActivity {
    TextView   dateselect, dateselectF;
    EditText   datedebut, datefin;
    DatePickerDialog.OnDateSetListener setListener;
    String[] items = {"Consoles & Jeux vidéo", "High-Tech", "Epicerie & Courses", "Mode & Accessories", "Santé & Cosmétiques", "Maison & Habitat", "Auto-Moto"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imagePaths;
    private DealService dealService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_deals);

        dateselect = findViewById(R.id.datedebut);
        datedebut = findViewById(R.id.datededebut);
        dateselectF = findViewById(R.id.datefin);
        datefin = findViewById(R.id.datedefin);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        dateselect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreatDeals.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();


            }

        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
              month = month+1;
              String date = day+"/"+month+"/"+year;
              datedebut.setText(date);
            }
        };
        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreatDeals.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateselect.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        dateselectF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreatDeals.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();


            }

        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                datefin.setText(date);
            }
        };
        dateselectF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreatDeals.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dateselectF.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //categorie logique

        autoCompleteTextView = findViewById(R.id.categories);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(CreatDeals.this, "Categorie:" + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* public void onSubmitClick(View view) {
        // Traitement à effectuer lors du clic sur check cad save

    }*/


}

// text view c'est tv_date  datedebut Date selectioné
// edittext et_date datedefin