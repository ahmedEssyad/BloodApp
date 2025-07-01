package com.example.bloodapp.user;

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

import com.example.bloodapp.BloodRequestActivity;
import com.example.bloodapp.DonorFormActivity;
import com.example.bloodapp.R;
import com.example.bloodapp.RequestListActivity;
import com.example.bloodapp.auth.LoginActivity;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.DonationHistory;
import com.example.bloodapp.models.User;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvBloodGroup, tvTotalDonations, tvLastDonation, tvNextEligible;
    private CardView cardDonate, cardMyDonations, cardFindDonors, cardBloodRequests;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        setupClickListeners();
        loadUserData();
        loadDonationHistory();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvTotalDonations = findViewById(R.id.tvTotalDonations);
        tvLastDonation = findViewById(R.id.tvLastDonation);
        tvNextEligible = findViewById(R.id.tvNextEligible);
        
        cardDonate = findViewById(R.id.cardDonate);
        cardMyDonations = findViewById(R.id.cardMyDonations);
        cardFindDonors = findViewById(R.id.cardFindDonors);
        cardBloodRequests = findViewById(R.id.cardBloodRequests);
        
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        cardDonate.setOnClickListener(v -> {
            startActivity(new Intent(this, DonateBloodActivity.class));
        });

        cardMyDonations.setOnClickListener(v -> {
            startActivity(new Intent(this, MyDonationsActivity.class));
        });

        cardFindDonors.setOnClickListener(v -> {
            startActivity(new Intent(this, FindDonorsActivity.class));
        });

        cardBloodRequests.setOnClickListener(v -> {
            startActivity(new Intent(this, RequestListActivity.class));
        });
    }

    private void loadUserData() {
        String token = "Bearer " + sessionManager.getToken();
        
        apiService.getCurrentUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvWelcome.setText("Bienvenue, " + user.getFullName());
                    tvBloodGroup.setText("Groupe sanguin : " + user.getBloodGroup());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserDashboardActivity.this, 
                    "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDonationHistory() {
        progressBar.setVisibility(View.VISIBLE);
        String token = "Bearer " + sessionManager.getToken();
        String userId = sessionManager.getUserId();

        apiService.getDonationHistory(token, userId).enqueue(new Callback<List<DonationHistory>>() {
            @Override
            public void onResponse(Call<List<DonationHistory>> call, Response<List<DonationHistory>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<DonationHistory> donations = response.body();
                    updateDonationStats(donations);
                }
            }

            @Override
            public void onFailure(Call<List<DonationHistory>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserDashboardActivity.this, 
                    "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDonationStats(List<DonationHistory> donations) {
        tvTotalDonations.setText(String.valueOf(donations.size()));
        
        if (!donations.isEmpty()) {
            DonationHistory lastDonation = donations.get(0);
            tvLastDonation.setText(formatDate(lastDonation.getDonationDate()));
            
            // Calculer la prochaine date éligible (56 jours après la dernière donation)
            // À implémenter avec java.time ou Calendar
        } else {
            tvLastDonation.setText("Aucune donation");
            tvNextEligible.setText("Éligible maintenant");
        }
    }
    
    private String formatDate(String dateString) {
        // Formater la date de manière lisible
        return dateString; // À implémenter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
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
