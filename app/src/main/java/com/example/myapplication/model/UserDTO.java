package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDTO implements Parcelable {

    private Long id;
    private String userName;
    private String profileImageUrl;

    // Constructors, getters, setters, etc.

    // Add the new attribute to the constructor
    public UserDTO(Long id, String userName, String profileImageUrl) {
        this.id = id;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }

    // Modify writeToParcel to include the new attribute
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id); // Use writeValue for Long
        dest.writeString(userName);
        dest.writeString(profileImageUrl);
    }

    // Modify the constructor to read the new attribute
    protected UserDTO(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        userName = in.readString();
        profileImageUrl = in.readString();
    }

    // Implement Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters for the new attribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // Parcelable CREATOR
    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };
}
