package com.example.myapplication.model;

import android.os.Parcel;
import android.util.Log;
import android.os.Parcelable;

import com.example.myapplication.model.Enum.Categories;

import java.util.Collection;

public class Discussion implements Parcelable{
    private Long id;
    private String titre;
    private String description;
    private Categories categorie;
    private int nbrvue;
    private String profileImageUrl;
    private int save;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getSave() {
        return save;
    }

    public Categories getCategorie() {
        return categorie;
    }

    public void setCategorie(Categories categorie) {
        this.categorie = categorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    Categories categories;
    private String createurUsername;

    public String getCreateurUsername() {
        return createurUsername;
    }

    public void setCreateurUsername(String createurUsername) {
        this.createurUsername = createurUsername;
    }

    private Collection<MessageDTO> discMessage;


    public Discussion(Long id, String titre, String description, int nbrvue, Categories categories, String createurUsername) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.nbrvue = nbrvue;
        this.categories = categories;
        this.createurUsername = createurUsername;
        Log.d("Discussion", "Discussion created with Createur: " + createurUsername);
    }

    public Discussion() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbrvue() {
        return nbrvue;
    }

    public void setNbrvue(int nbrvue) {
        this.nbrvue = nbrvue;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }




    public Collection<MessageDTO> getDiscMessage() {
        return discMessage;
    }

    public void setDiscMessage(Collection<MessageDTO> discMessage) {
        this.discMessage = discMessage;
    }


    // Implémentation Parcelable
    protected Discussion(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        titre = in.readString();
        description = in.readString();
        nbrvue = in.readInt();
        categories = in.readParcelable(Categories.class.getClassLoader());
        createurUsername = in.readString();
        // ... Lisez les autres champs depuis Parcel si nécessaire
    }

    public static final Creator<Discussion> CREATOR = new Creator<Discussion>() {
        @Override
        public Discussion createFromParcel(Parcel in) {
            return new Discussion(in);
        }

        @Override
        public Discussion[] newArray(int size) {
            return new Discussion[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(titre);
        dest.writeString(description);
        dest.writeInt(nbrvue);

        // Écriture de l'énumération Categories sous forme de chaîne de caractères
        dest.writeString(categories != null ? categories.name() : null);

        dest.writeString(createurUsername);
        // ... Écrivez les autres champs dans Parcel si nécessaire
    }



    @Override
    public int describeContents() {
        return 0;
    }



}





