package com.example.myapplication.model;

import java.time.LocalDateTime;

public class UserProfileStatisticsDTO {
    private Long id;
    private String userName;

    public UserProfileStatisticsDTO(Long id, String userName, String profileImageUrl, int numberOfDeals, int numberOfSavedDis, LocalDateTime dateJoined) {
        this.id = id;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.numberOfDeals = numberOfDeals;
        this.numberOfSavedDis = numberOfSavedDis;
        DateJoined = dateJoined;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setNumberOfDeals(int numberOfDeals) {
        this.numberOfDeals = numberOfDeals;
    }

    public void setNumberOfSavedDis(int numberOfSavedDis) {
        this.numberOfSavedDis = numberOfSavedDis;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        DateJoined = dateJoined;
    }

    private String profileImageUrl;
    private int numberOfDeals;
    private int numberOfSavedDis;
    private LocalDateTime DateJoined;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getNumberOfDeals() {
        return numberOfDeals;
    }

    public int getNumberOfSavedDis() {
        return numberOfSavedDis;
    }

    public LocalDateTime getDateJoined() {
        return DateJoined;
    }

}
