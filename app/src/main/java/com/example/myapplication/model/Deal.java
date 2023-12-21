package com.example.myapplication.model;

import com.example.myapplication.model.Enum.Categories;

import java.util.Date;

public class Deal {

    private String title;

    private String description;

    private String lienDeal;

    private Categories category;

    private Date dateDebut;

    private Date dateFin;

    private float price;

    private float newPrice;

    private String localisation;

    private boolean deliveryExist;

    private float deliveryPrice;


    public Deal(String title, String description, String lienDeal, float price, float newPrice, String localisation,Categories category,boolean deliveryExist ) {
        this.title = title;
        this.description = description;
        this.lienDeal = lienDeal;
        this.price = price;
        this.newPrice = newPrice;
        this.localisation = localisation;
        this.category = category;
        this.deliveryExist = deliveryExist;
    }
}
