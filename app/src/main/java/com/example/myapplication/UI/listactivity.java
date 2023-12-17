package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.myapplication.R;
import com.example.myapplication.UI.adapters.listAdapter;
import com.example.myapplication.model.listData;

import java.util.ArrayList;

public class listactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_deals);

        // Crée une liste fictive d'objets listData pour affichage dans la liste
        ArrayList<listData> dataArrayList = createDummyData();

        // Utilise l'adapter pour afficher la liste de deals
        listAdapter adapter = new listAdapter(this, dataArrayList);
        ListView listView = findViewById(R.id.cardView);
        listView.setAdapter(adapter);
    }

    private ArrayList<listData> createDummyData() {
        // Crée une liste fictive d'objets listData pour simuler la liste réelle de deals

        ArrayList<listData> dummyDataList = new ArrayList<>();

        // Ajoute des objets listData fictifs à la liste
        dummyDataList.add(new listData("Titre Deal 1", "Temps 1", "Description Deal 1", R.drawable.login, "Prix Normal 1", "Prix After 1", true));
        dummyDataList.add(new listData("Titre Deal 2", "Temps 2", "Description Deal 2", R.drawable.plus, "Prix Normal 2", "Prix After 2", false));
        // Ajoute d'autres deals fictifs si nécessaire

        return dummyDataList;
    }
}

