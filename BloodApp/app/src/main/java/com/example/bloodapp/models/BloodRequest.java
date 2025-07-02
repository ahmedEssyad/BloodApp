package com.example.bloodapp.models;

public class BloodRequest {
    private final String hospital;
    private final String bloodType;
    private final int unitsNeeded;
    private final String urgencyLevel;
    private final ContactPerson contactPerson;
//    private final boolean isFulfilled = false;

    public BloodRequest(String hospital, String bloodType, int unitsNeeded, String urgencyLevel, ContactPerson contactPerson) {
        this.hospital = hospital;
        this.bloodType = bloodType;
        this.unitsNeeded = unitsNeeded;
        this.urgencyLevel = urgencyLevel;
        this.contactPerson = contactPerson;
    }

    public String getHospital() {
        return hospital;
    }
    
    public String getHospitalName() {
        return hospital;
    }
    
    public String getLocation() {
        return hospital; // Using hospital as location for now
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getUnitsNeeded() {
        return unitsNeeded;
    }

//   public boolean isFulfilled() {
//        return isFulfilled;
//    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    // ✅ Classe interne ContactPerson
    public static class ContactPerson {
        private final String name;
        private final String phone;

        public ContactPerson(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }
}
