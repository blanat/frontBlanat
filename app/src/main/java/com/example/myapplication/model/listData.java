package com.example.myapplication.model;

public class listData {
    String titre, time;
    int ingredients, desc;
    int image;
    String prixN, prixA;

     Boolean livraison;



    public listData(String titre, String time, int desc, int image, String prixN, String prixA, Boolean livraison) {
        this.titre = titre;
        this.time = time;

        this.desc = desc;
        this.image = image;
        this.prixN = prixN;
        this.prixA = prixA;
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


    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
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
