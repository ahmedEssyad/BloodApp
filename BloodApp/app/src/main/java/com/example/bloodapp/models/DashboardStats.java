package com.example.bloodapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardStats {
    @SerializedName("totalUsers")
    private int totalUsers;
    
    @SerializedName("totalDonors")
    private int totalDonors;
    
    @SerializedName("totalRequests")
    private int totalRequests;
    
    @SerializedName("activeRequests")
    private int activeRequests;
    
    @SerializedName("totalDonations")
    private int totalDonations;
    
    @SerializedName("bloodGroupStats")
    private List<BloodGroupStat> bloodGroupStats;
    
    @SerializedName("recentRequests")
    private List<BloodRequest> recentRequests;
    
    public int getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public int getTotalDonors() {
        return totalDonors;
    }
    
    public void setTotalDonors(int totalDonors) {
        this.totalDonors = totalDonors;
    }
    
    public int getTotalRequests() {
        return totalRequests;
    }
    
    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }
    
    public int getActiveRequests() {
        return activeRequests;
    }
    
    public void setActiveRequests(int activeRequests) {
        this.activeRequests = activeRequests;
    }
    
    public int getTotalDonations() {
        return totalDonations;
    }
    
    public void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }
    
    public List<BloodGroupStat> getBloodGroupStats() {
        return bloodGroupStats;
    }
    
    public void setBloodGroupStats(List<BloodGroupStat> bloodGroupStats) {
        this.bloodGroupStats = bloodGroupStats;
    }
    
    public List<BloodRequest> getRecentRequests() {
        return recentRequests;
    }
    
    public void setRecentRequests(List<BloodRequest> recentRequests) {
        this.recentRequests = recentRequests;
    }
    
    public static class BloodGroupStat {
        @SerializedName("_id")
        private String bloodGroup;
        
        @SerializedName("count")
        private int count;
        
        public String getBloodGroup() {
            return bloodGroup;
        }
        
        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
    }
}
