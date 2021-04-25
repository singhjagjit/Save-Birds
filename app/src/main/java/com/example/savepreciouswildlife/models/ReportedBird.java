package com.example.savepreciouswildlife.models;

public class ReportedBird {
    private String id;
    private String reporterEmail;
    private String province;
    private String city;
    private String species;
    private String injury;

    public ReportedBird(String id, String reporterEmail, String province, String city, String species, String injury) {
        this.id = id;
        this.reporterEmail = reporterEmail;
        this.province = province;
        this.city = city;
        this.species = species;
        this.injury = injury;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterName) {
        this.reporterEmail = reporterEmail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getInjury() {
        return injury;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }
}
