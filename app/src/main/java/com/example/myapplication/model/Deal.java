package com.example.myapplication.model;

import com.example.myapplication.model.Enum.Categories;

import java.time.LocalDate;
import java.util.Date;

public class Deal {

    private String title;

    private String description;

    private String lienDeal;

    private Categories category;

    private String dateDebut;

    private String dateFin;

    private float price;

    private float newPrice;

    private String localisation;

    private boolean deliveryExist;

    private float deliveryPrice;


    public Deal(String title, String description, String lienDeal, float price, float newPrice, String localisation,Categories category,boolean deliveryExist,float deliveryPrice,String dateDebut,String dateFin ) {
        this.title = title;
        this.description = description;
        this.lienDeal = lienDeal;
        this.price = price;
        this.newPrice = newPrice;
        this.localisation = localisation;
        this.category = category;
        this.deliveryExist = deliveryExist;
        this.deliveryPrice = deliveryPrice;
        //newly added attributs
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

    }
}
