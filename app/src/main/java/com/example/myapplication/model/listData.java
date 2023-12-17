package com.example.myapplication.model;

import java.time.LocalDateTime;

public class listData {

    //images problem
 /*   private String title;
    private String description;

    //pour filtrage seulement
    private Categories category;

    //en backend si le deal depasse date_fin il sera supprimee
    //private Date date_debut;
    //private Date date_fin;
    private String prixN;//prix nouveau
    private String prixA;//prix ancien
    private String localisation;
    private boolean livraisonExist;
    private float livraison_prix;
    private int nbre_comment;
    private int deg;

    //pour calculer time dans listage des deal
    private date dateCreation;

    private string lienDeal;

    //problem des commentaire: (comments avec la photo profil+nom)

*/



    String titre, time, desc;
    int image;
    String prixN, prixA;

     Boolean livraison;




    public listData(String titre, String time, String desc, int image, String prixN, String prixA, Boolean livraison) {
        this.titre = titre;
        this.time = time;
        this.desc = desc;
        this.image = image;
        this.prixN = prixN;
        this.prixA = prixA;
        this.livraison = livraison;

    }
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrixN() {
        return prixN;
    }

    public void setPrixN(String prixN) {
        this.prixN = prixN;
    }

    public String getPrixA() {
        return prixA;
    }

    public void setPrixA(String prixA) {
        this.prixA = prixA;
    }

    public Boolean getLivraison() {
        return livraison;
    }

    public void setLivraison(Boolean livraison) {
        this.livraison = livraison;
    }




}
