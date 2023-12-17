package com.example.myapplication.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class Detailsactivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsactivity);


        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailDescription = findViewById(R.id.dealdesc);
        TextView detailPrice = findViewById(R.id.prixN);
        TextView detailPriceA = findViewById(R.id.prixA);
        Button dealButton = findViewById(R.id.DealButton);
        TextView commentaire = findViewById(R.id.comment);

        // Récupère les données passées depuis l'intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            int imageResId = extras.getInt("imageResId");
            String price = extras.getString("price");

            // Met à jour les vues avec les données
            String dealtitle = detailTitle.getText().toString();




            // Gère le clic sur le bouton "Voir Deal"
            dealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ajoute le code pour gérer le clic sur le bouton ici
                }
            });
        }
    }
}
