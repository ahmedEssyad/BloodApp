package com.example.bloodapp.models;

import com.google.gson.annotations.SerializedName;

public class RoleUpdateRequest {
    @SerializedName("role")
    private String role;
    
    public RoleUpdateRequest(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
