package com.example.savepreciouswildlife.models;

// @author Kateryna
//created class to keep the patient objects

public class Patient {
    public int idPatient;
    public int picPatient;
    public String birdName;
    public String birdBreed;
    public String birdInjure;
    public int sumNeed;
    public int sumDonated;
    public int sumLeft;

    public Patient(int idPatient,int picPatient, String birdName, String birdBreed, String birdInjure, int sumNeed, int sumDonated, int sumLeft) {
        this.idPatient = idPatient;
        this.picPatient = picPatient;
        this.birdName = birdName;
        this.birdBreed = birdBreed;
        this.birdInjure = birdInjure;
        this.sumNeed = sumNeed;
        this.sumDonated = sumDonated;
        this.sumLeft = sumLeft;
    }
    public Patient(int idPatient, int sumNeed,int sumDonated,int sumLeft) {
        this.idPatient = idPatient;
        this.sumNeed = sumNeed;
        this.sumDonated = sumDonated;
        this.sumLeft = sumLeft;
    }
    public Patient(int idPatient) {
        this.idPatient = idPatient;
    }


    public int getPicPatient() {
        return picPatient;
    }

    public void setPicPatient(int picPatient) {
        this.picPatient = picPatient;
    }

    public String getBirdName() {
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public String getBirdBreed() {
        return birdBreed;
    }

    public void setBirdBreed(String birdBreed) {
        this.birdBreed = birdBreed;
    }

    public String getBirdInjure() {
        return birdInjure;
    }

    public void setBirdInjure(String birdInjure) {
        this.birdInjure = birdInjure;
    }

    public int getSumNeed() {
        return sumNeed;
    }

    public void setSumNeed(int sumNeed) {
        this.sumNeed = sumNeed;
    }

    public int getSumDonated() {
        return sumDonated;
    }

    public void setSumDonated(int sumDonated) {
        this.sumDonated = sumDonated;
    }

    public int getSumLeft() {
        return sumLeft;
    }

    public void setSumLeft(int sumLeft) {
        this.sumLeft = sumLeft;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }
}
