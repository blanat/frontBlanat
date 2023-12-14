package com.example.myapplication.model;

import com.example.myapplication.model.Enum.Categories;

import java.util.Date;

public class Deal {
    private String title;
    private String description;

    //faites attention au nom des var ne les changer pas
    // pour ne pas avoir un conflit avec backend
/*
    private Categories category;
    private Date date_debut;
    private Date date_fin;
    private String prix;
    private String localisation;
    private boolean livraisonExist;
    private float livraison_prix;
*/

    

    public Deal(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
