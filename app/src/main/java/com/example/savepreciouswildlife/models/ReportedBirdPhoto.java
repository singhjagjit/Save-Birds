package com.example.savepreciouswildlife.models;

public class ReportedBirdPhoto {
    private String photoUrl;
    private String reportedBirdId;

    public ReportedBirdPhoto(){

    }

    public ReportedBirdPhoto(String photoUrl, String reportedBirdId) {
        this.photoUrl = photoUrl;
        this.reportedBirdId = reportedBirdId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReportedBirdId() {
        return reportedBirdId;
    }

    public void setReportedBirdId(String reportedBirdId) {
        this.reportedBirdId = reportedBirdId;
    }
}
