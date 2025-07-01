package com.example.bloodapp.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.DonationHistory;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDonationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvNoDonations;
    private ProgressBar progressBar;
    private DonationHistoryAdapter adapter;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);

        setTitle("Mes donations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        loadDonationHistory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvNoDonations = findViewById(R.id.tvNoDonations);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonationHistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
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
                    if (donations.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvNoDonations.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNoDonations.setVisibility(View.GONE);
                        adapter.updateDonations(donations);
                    }
                } else {
                    Toast.makeText(MyDonationsActivity.this, 
                        "Erreur lors du chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DonationHistory>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyDonationsActivity.this, 
                    "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
