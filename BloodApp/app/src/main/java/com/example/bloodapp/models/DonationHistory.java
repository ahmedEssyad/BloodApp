package com.example.bloodapp.models;

import java.util.Date;

public class DonationHistory {
    private String id;
    private String donorId;
    private String donationCenter;
    private String donationDate;
    private int unitsGiven;
    private String recipientHospital;
    private String notes;

    public DonationHistory(String donorId, String donationCenter, String donationDate, int unitsGiven, String recipientHospital, String notes) {
        this.donorId = donorId;
        this.donationCenter = donationCenter;
        this.donationDate = donationDate;
        this.unitsGiven = unitsGiven;
        this.recipientHospital = recipientHospital;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getDonationCenter() {
        return donationCenter;
    }

    public void setDonationCenter(String donationCenter) {
        this.donationCenter = donationCenter;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    public int getUnitsGiven() {
        return unitsGiven;
    }

    public void setUnitsGiven(int unitsGiven) {
        this.unitsGiven = unitsGiven;
    }

    public String getRecipientHospital() {
        return recipientHospital;
    }

    public void setRecipientHospital(String recipientHospital) {
        this.recipientHospital = recipientHospital;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getLocation() {
        return donationCenter;
    }
    
    public int getUnitsdonated() {
        return unitsGiven;
    }
    
    public String getStatus() {
        return "completed"; // Default status
    }
}
