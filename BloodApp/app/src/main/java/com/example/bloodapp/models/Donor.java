package com.example.bloodapp.models;


public class Donor {
    private final String name;
    private final String phone;
    private final String bloodType;
    private final String location;
    private final String email;
    private final String lastDonation;

    // Constructeur
    public Donor(String name, String phone, String bloodType, String location, String email, String lastDonation) {
        this.name = name;
        this.phone = phone;
        this.bloodType = bloodType;
        this.location = location;
        this.email = email;
        this.lastDonation = lastDonation;
    }

    // Getters et Setters (tu peux les générer automatiquement)
}

