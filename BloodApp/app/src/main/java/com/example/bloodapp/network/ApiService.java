package com.example.bloodapp.network;

import com.example.bloodapp.auth.AuthRequest;
import com.example.bloodapp.auth.AuthResponse;
import com.example.bloodapp.auth.RegisterRequest;
import com.example.bloodapp.models.AdminCreateRequest;
import com.example.bloodapp.models.DashboardStats;
import com.example.bloodapp.models.Donor;
import com.example.bloodapp.models.BloodRequest;
import com.example.bloodapp.models.DonationHistory;
import com.example.bloodapp.models.Badge;
import com.example.bloodapp.models.PasswordChangeRequest;
import com.example.bloodapp.models.RoleUpdateRequest;
import com.example.bloodapp.models.StatusUpdateRequest;
import com.example.bloodapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Authentication endpoints (Express typique)
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    // User endpoints (avec Mongoose)
    @GET("api/users/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @GET("api/users/{id}")
    Call<User> getUser(@Header("Authorization") String token, @Path("id") String userId);

    // Donor endpoints (adaptés pour Mongoose)
    @POST("api/donors")
    Call<Donor> createDonor(@Body Donor donor);

    @GET("api/donors/{id}")
    Call<Donor> getDonorProfile(@Header("Authorization") String token, @Path("id") String donorId);

    @GET("api/donors")
    Call<List<Donor>> getNearbyDonors(
        @Query("bloodGroup") String bloodGroup,
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("radius") int radius
    );

    // Blood request endpoints (adaptés pour Mongoose)
    @POST("api/blood-requests")
    Call<BloodRequest> createBloodRequest(@Body BloodRequest request);

    @GET("api/blood-requests")
    Call<List<BloodRequest>> getAllRequests();

    @GET("api/blood-requests/active")
    Call<List<BloodRequest>> getActiveRequests();

    // Donation history endpoints
    @GET("api/donors/{id}/donations")
    Call<List<DonationHistory>> getDonationHistory(@Header("Authorization") String token, @Path("id") String donorId);

    @POST("api/donors/{id}/donations")
    Call<DonationHistory> addDonation(@Header("Authorization") String token, @Path("id") String donorId, @Body DonationHistory donation);

    // Badges endpoints (optionnel)
    @GET("api/donors/{id}/badges")
    Call<List<Badge>> getDonorBadges(@Header("Authorization") String token, @Path("id") String donorId);

    // Admin endpoints
    @GET("api/admin/dashboard")
    Call<DashboardStats> getDashboardStats(@Header("Authorization") String token);
    
    @GET("api/admin/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);
    
    @PUT("api/admin/users/{userId}/role")
    Call<User> updateUserRole(@Header("Authorization") String token, @Path("userId") String userId, @Body RoleUpdateRequest roleRequest);
    
    @DELETE("api/admin/users/{userId}")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("userId") String userId);
    
    @GET("api/admin/blood-requests")
    Call<List<BloodRequest>> getAllBloodRequests(@Header("Authorization") String token);
    
    @PUT("api/admin/blood-requests/{requestId}/status")
    Call<BloodRequest> updateRequestStatus(@Header("Authorization") String token, @Path("requestId") String requestId, @Body StatusUpdateRequest statusRequest);
    
    @GET("api/admin/donors")
    Call<List<Donor>> getAllDonors(@Header("Authorization") String token);
    
    @POST("api/admin/create-admin")
    Call<User> createAdmin(@Header("Authorization") String token, @Body AdminCreateRequest adminRequest);
    
    @PUT("api/admin/change-password")
    Call<Void> changeAdminPassword(@Header("Authorization") String token, @Body PasswordChangeRequest passwordRequest);
}
