package com.example.bloodapp.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.BloodRequest;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonateBloodActivity extends AppCompatActivity {

    private TextView tvEligibilityStatus;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btnScheduleDonation;
    private ApiService apiService;
    private SessionManager sessionManager;
    private BloodRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);

        setTitle("Faire un don");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        checkEligibility();
        loadBloodRequests();
    }

    private void initViews() {
        tvEligibilityStatus = findViewById(R.id.tvEligibilityStatus);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnScheduleDonation = findViewById(R.id.btnScheduleDonation);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BloodRequestAdapter(new ArrayList<>(), this::onRequestSelected);
        recyclerView.setAdapter(adapter);

        btnScheduleDonation.setOnClickListener(v -> {
            // Implémenter la planification de don
            Toast.makeText(this, "Fonctionnalité à venir", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkEligibility() {
        // Vérifier l'éligibilité du donneur
        // À implémenter avec l'API
        tvEligibilityStatus.setText("Vous êtes éligible pour donner du sang");
        btnScheduleDonation.setEnabled(true);
    }

    private void loadBloodRequests() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getActiveRequests().enqueue(new Callback<List<BloodRequest>>() {
            @Override
            public void onResponse(Call<List<BloodRequest>> call, Response<List<BloodRequest>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateRequests(response.body());
                } else {
                    Toast.makeText(DonateBloodActivity.this, 
                        "Erreur lors du chargement des demandes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BloodRequest>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DonateBloodActivity.this, 
                    "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onRequestSelected(BloodRequest request) {
        // Implémenter la sélection d'une demande
        Toast.makeText(this, "Demande sélectionnée : " + request.getHospitalName(), 
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
