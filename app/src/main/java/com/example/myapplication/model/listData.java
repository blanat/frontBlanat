package com.example.myapplication.model;

public class listData {
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
