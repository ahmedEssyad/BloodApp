package com.example.bloodapp.models;

public class Badge {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String criteria;
    private String earningDate;

    public Badge(String name, String description, String imageUrl, String criteria, String earningDate) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.criteria = criteria;
        this.earningDate = earningDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getEarningDate() {
        return earningDate;
    }

    public void setEarningDate(String earningDate) {
        this.earningDate = earningDate;
    }
}
