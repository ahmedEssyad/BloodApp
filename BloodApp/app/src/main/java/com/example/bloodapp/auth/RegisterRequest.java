package com.example.bloodapp.auth;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String bloodGroup;
    private int age;
    private String location;

    public RegisterRequest() {
        // Constructeur par défaut nécessaire pour Retrofit
    }

    public RegisterRequest(String fullName, String email, String password, String phone, String bloodGroup, int age, String location) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.location = location;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
