package com.example.myapplication.model;

import android.util.Log;

import java.util.Date;

public class listData {
    long deal_ID;
    Date date_fin;
    String title, description, time, localisation, lienDeal, firstImageURL, timePassedSinceCreation;
    int nbre_comment, deg;
    String prix_N, prix_A;
    float livraison_prix;
    Boolean livraisonExist;

    public listData(long deal_ID, Date date_fin, String title, String description, String time, String localisation, String lienDeal, String firstImageURL, String timePassedSinceCreation, int nbre_comment, int deg, String prix_N, String prix_A, float livraison_prix, Boolean livraisonExist) {
        this.deal_ID = deal_ID;
        this.date_fin = date_fin;
        this.title = title;
        this.description = description;
        this.time = time;
        this.localisation = localisation;
        this.lienDeal = lienDeal;
        this.firstImageURL = firstImageURL;
        this.timePassedSinceCreation = timePassedSinceCreation;
        this.nbre_comment = nbre_comment;
        this.deg = deg;
        this.prix_N = prix_N;
        this.prix_A = prix_A;
        this.livraison_prix = livraison_prix;
        this.livraisonExist = livraisonExist;
    }

    public long getDeal_ID() {
        return deal_ID;
    }

    public void setDeal_ID(long deal_ID) {
        this.deal_ID = deal_ID;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getLienDeal() {
        return lienDeal;
    }

    public void setLienDeal(String lienDeal) {
        this.lienDeal = lienDeal;
    }

    public String getfirstImageURL() {
        Log.d("Image URL Getter", "Getting Image URL: " + firstImageURL);
        return firstImageURL;
    }

    public void setfirstImageURL(String firstImageURL) {
        this.firstImageURL = firstImageURL;
    }

    public String getTimePassedSinceCreation() {
        return timePassedSinceCreation;
    }

    public void setTimePassedSinceCreation(String timePassedSinceCreation) {
        this.timePassedSinceCreation = timePassedSinceCreation;
    }

    public int getNbre_comment() {
        return nbre_comment;
    }

    public void setNbre_comment(int nbre_comment) {
        this.nbre_comment = nbre_comment;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public String getPrix_N() {
        return prix_N;
    }

    public void setPrix_N(String prix_N) {
        this.prix_N = prix_N;
    }

    public String getPrix_A() {
        return prix_A;
    }

    public void setPrix_A(String prix_A) {
        this.prix_A = prix_A;
    }

    public float getLivraison_prix() {
        return livraison_prix;
    }

    public void setLivraison_prix(float livraison_prix) {
        this.livraison_prix = livraison_prix;
    }

    public Boolean getLivraisonExist() {
        return livraisonExist;
    }

    public void setLivraisonExist(Boolean livraisonExist) {
        this.livraisonExist = livraisonExist;
    }
}
