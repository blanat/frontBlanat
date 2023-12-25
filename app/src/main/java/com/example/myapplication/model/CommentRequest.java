package com.example.myapplication.model;

public class CommentRequest {

    private long dealId;
    private String content;

    // Constructors
    public CommentRequest() {
    }

    public CommentRequest(long dealId, String content) {
        this.dealId = dealId;
        this.content = content;
    }

    // Getters and Setters
    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
