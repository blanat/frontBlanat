package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.myapplication.model.Enum.Categories;

import java.util.Date;

public class listData implements Parcelable {

    private long dealID;
    private String title;
    private String description;
    private Categories category;
    private Date dateFin;
    private float price;
    private float newPrice;
    private String localisation;
    private boolean deliveryExist;
    private float deliveryPrice;
    private int deg;
    private int numberOfComments;
    private String lienDeal;

    private UserDTO dealCreator; // Add this field


    // Image and time will be added to the DTO
        private String firstImageUrl;
        private String timePassedSinceCreation; // corresponds to the same name in the entity


    public listData(long dealID, String title, String description, Categories category, Date dateFin, float price,
                       float newPrice, String localisation, boolean deliveryExist, float deliveryPrice, int deg,
                       int numberOfComments, String lienDeal, String firstImageUrl, String timePassedSinceCreation) {
        this.dealID = dealID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.dateFin = dateFin;
        this.price = price;
        this.newPrice = newPrice;
        this.localisation = localisation;
        this.deliveryExist = deliveryExist;
        this.deliveryPrice = deliveryPrice;
        this.deg = deg;
        this.numberOfComments = numberOfComments;
        this.lienDeal = lienDeal;
        this.firstImageUrl = firstImageUrl;
        this.timePassedSinceCreation = timePassedSinceCreation;
    }
    // Getters and setters

    public long getDealID() {
        return dealID;
    }

    public void setDealID(long dealID) {
        this.dealID = dealID;
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

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public boolean isDeliveryExist() {
        return deliveryExist;
    }

    public void setDeliveryExist(boolean deliveryExist) {
        this.deliveryExist = deliveryExist;
    }

    public float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(float deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public String getLienDeal() {
        return lienDeal;
    }

    public void setLienDeal(String lienDeal) {
        this.lienDeal = lienDeal;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getTimePassedSinceCreation() {
        return timePassedSinceCreation;
    }

    public void setTimePassedSinceCreation(String timePassedSinceCreation) {
        this.timePassedSinceCreation = timePassedSinceCreation;
    }


    public UserDTO getDealCreator() {
        return dealCreator;
    }


    //===============================================================
    protected listData(Parcel in) {
        dealID = in.readLong();
        title = in.readString();
        description = in.readString();
        category = Categories.valueOf(in.readString());
        //dateFin = new Date(in.readLong());
        price = in.readFloat();
        newPrice = in.readFloat();
        localisation = in.readString();
        deliveryExist = in.readByte() != 0;
        deliveryPrice = in.readFloat();
        deg = in.readInt();
        numberOfComments = in.readInt();
        lienDeal = in.readString();
        firstImageUrl = in.readString();
        timePassedSinceCreation = in.readString();
        dealCreator = in.readParcelable(UserDTO.class.getClassLoader()); // Read UserDTO

    }


    public static final Parcelable.Creator<listData> CREATOR = new Parcelable.Creator<listData>() {
        @Override
        public listData createFromParcel(Parcel in) {
            return new listData(in);
        }

        @Override
        public listData[] newArray(int size) {
            return new listData[size];
        }
    };

    // Add other getters and setters as needed

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dealID);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category.name());
//        dest.writeLong(dateFin.getTime());
        dest.writeFloat(price);
        dest.writeFloat(newPrice);
        dest.writeString(localisation);
        dest.writeByte((byte) (deliveryExist ? 1 : 0));
        dest.writeFloat(deliveryPrice);
        dest.writeInt(deg);
        dest.writeInt(numberOfComments);
        dest.writeString(lienDeal);
        dest.writeString(firstImageUrl);
        dest.writeString(timePassedSinceCreation);
        dest.writeParcelable(dealCreator, flags); // Write UserDTO

    }


    public int describeContents() {
        return 0;
    }


}
