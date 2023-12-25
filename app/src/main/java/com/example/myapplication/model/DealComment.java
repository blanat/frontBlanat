package com.example.myapplication.model;
import java.time.LocalDateTime;

public class DealComment {

    private Long id;
    private String content;
    private String timeSincePosted;
    private UserDTO dealCreator;

    // Constructors
    public DealComment() {
    }

    public DealComment(Long id, String content, String timeSincePosted, UserDTO sender) {
        this.id = id;
        this.content = content;
        this.timeSincePosted = timeSincePosted;
        this.dealCreator = sender;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSincePosted() {
        return timeSincePosted;
    }

    public void setTimeSincePosted(String timeSincePosted) {
        this.timeSincePosted = timeSincePosted;
    }

    public UserDTO getSender() {
        return dealCreator;
    }

    public void setSender(UserDTO sender) {
        this.dealCreator = sender;
    }
}
