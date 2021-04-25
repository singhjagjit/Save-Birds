package com.example.savepreciouswildlife.models;


/**
 * @author JunHyungKim
 * This model class is for saving or receiving New befamiliar bird object later.
 */
public class BeFamiliarBird {
    private String id;
    private String province;
    private String city;
    private String name;
    private String etc;

    public BeFamiliarBird(String id, String province, String city, String name, String etc) {
        this.id = id;
        this.province = province;
        this.city = city;
        this.name = name;
        this.etc = etc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
