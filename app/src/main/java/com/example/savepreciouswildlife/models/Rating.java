package com.example.savepreciouswildlife.models;

public class Rating {
    private String volunteerID;
    private String userEmail;
    private int rating;

    public Rating(String volunteerID, String userEmail, int rating) {
        this.volunteerID = volunteerID;
        this.userEmail = userEmail;
        this.rating = rating;
    }

    public String getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(String volunteerID) {
        this.volunteerID = volunteerID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
