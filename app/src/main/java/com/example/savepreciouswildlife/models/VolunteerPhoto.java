package com.example.savepreciouswildlife.models;

public class VolunteerPhoto {
    private String photoUrl;
    private String volunteerId;

    public VolunteerPhoto(){

    }

    public VolunteerPhoto(String photoUrl, String volunteerId) {
        this.photoUrl = photoUrl;
        this.volunteerId = volunteerId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }
}
