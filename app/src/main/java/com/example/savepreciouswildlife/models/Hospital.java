package com.example.savepreciouswildlife.models;

public class Hospital {
    private String name;
    private String city;
    private String province;
    private String address;

    public Hospital(String name, String city, String province, String address) {
        this.name = name;
        this.city = city;
        this.province = province;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
