package com.example.bloodapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bloodapp.user.UserDashboardActivity;
import com.example.bloodapp.models.BloodRequest;
import com.example.bloodapp.models.BloodRequest.ContactPerson;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnAddDonor, btnAddRequest, btnViewRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Rediriger vers le tableau de bord utilisateur
        Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
        startActivity(intent);
        finish();

        btnAddDonor = findViewById(R.id.btnAddDonor);
        btnAddRequest = findViewById(R.id.btnAddRequest);
        btnViewRequests = findViewById(R.id.btnViewRequests);

        btnAddDonor.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DonorFormActivity.class));
        });

        btnAddRequest.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BloodRequestActivity.class));
        });

        btnViewRequests.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RequestListActivity.class));
        });

        // ✅ Ajouter 3 demandes une seule fois au premier lancement
        SharedPreferences prefs = getSharedPreferences("initPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("requestsSeeded", false)) {

            ContactPerson contact1 = new ContactPerson("Docteur Sow", "22334455");
            ContactPerson contact2 = new ContactPerson("Dr. Dia", "99887766");
            ContactPerson contact3 = new ContactPerson("Dr. Sy", "55443322");

            BloodRequest r1 = new BloodRequest("Clinique El Wafa", "A+", 3, "medium", contact1);
            BloodRequest r2 = new BloodRequest("Hopital Centrale", "O-", 5, "high", contact2);
            BloodRequest r3 = new BloodRequest("Centre Médical Essalem", "B+", 2, "low", contact3);

            ApiService api = RetrofitClient.getClient().create(ApiService.class);

            // Create a reusable callback to avoid D8 issues with empty anonymous classes
            Callback<BloodRequest> seedCallback = createSeedCallback();
            
            api.createBloodRequest(r1).enqueue(seedCallback);
            api.createBloodRequest(r2).enqueue(seedCallback);
            api.createBloodRequest(r3).enqueue(seedCallback);

            // Marquer que les données ont été insérées
            prefs.edit().putBoolean("requestsSeeded", true).apply();
        }
    }
    
    private Callback<BloodRequest> createSeedCallback() {
        return new SeedDataCallback();
    }
    
    private static class SeedDataCallback implements Callback<BloodRequest> {
        @Override
        public void onResponse(Call<BloodRequest> call, Response<BloodRequest> response) {
            // Silent seed operation - no UI feedback needed
        }
        
        @Override
        public void onFailure(Call<BloodRequest> call, Throwable t) {
            // Silent failure - seed data is not critical
        }
    }
}
