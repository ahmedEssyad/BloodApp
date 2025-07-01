package com.example.bloodapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bloodapp.R;
import com.example.bloodapp.auth.LoginActivity;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.DashboardStats;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvTotalUsers, tvTotalDonors, tvTotalRequests, tvActiveRequests, tvTotalDonations;
    private CardView cardManageUsers, cardManageRequests, cardManageDonors, cardCreateAdmin;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        setupClickListeners();
        loadDashboardStats();
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalDonors = findViewById(R.id.tvTotalDonors);
        tvTotalRequests = findViewById(R.id.tvTotalRequests);
        tvActiveRequests = findViewById(R.id.tvActiveRequests);
        tvTotalDonations = findViewById(R.id.tvTotalDonations);
        
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardManageRequests = findViewById(R.id.cardManageRequests);
        cardManageDonors = findViewById(R.id.cardManageDonors);
        cardCreateAdmin = findViewById(R.id.cardCreateAdmin);
        
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        cardManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageUsersActivity.class));
        });

        cardManageRequests.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageBloodRequestsActivity.class));
        });

        cardManageDonors.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageDonorsActivity.class));
        });

        cardCreateAdmin.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateAdminActivity.class));
        });
    }

    private void loadDashboardStats() {
        progressBar.setVisibility(View.VISIBLE);
        String token = "Bearer " + sessionManager.getToken();

        apiService.getDashboardStats(token).enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStats stats = response.body();
                    updateUI(stats);
                } else {
                    Toast.makeText(AdminDashboardActivity.this, 
                        "Erreur lors du chargement des statistiques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminDashboardActivity.this, 
                    "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(DashboardStats stats) {
        tvTotalUsers.setText(String.valueOf(stats.getTotalUsers()));
        tvTotalDonors.setText(String.valueOf(stats.getTotalDonors()));
        tvTotalRequests.setText(String.valueOf(stats.getTotalRequests()));
        tvActiveRequests.setText(String.valueOf(stats.getActiveRequests()));
        tvTotalDonations.setText(String.valueOf(stats.getTotalDonations()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_change_password) {
            startActivity(new Intent(this, ChangePasswordActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
