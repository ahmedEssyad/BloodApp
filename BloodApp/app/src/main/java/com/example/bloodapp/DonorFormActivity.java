package com.example.bloodapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodapp.models.Donor;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorFormActivity extends AppCompatActivity {

    EditText nameInput, phoneInput, locationInput, emailInput, dateInput;
    Spinner bloodSpinner;
    Button submitBtn;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_form);

        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        locationInput = findViewById(R.id.location);
        emailInput = findViewById(R.id.email);
        dateInput = findViewById(R.id.lastDonation);
        bloodSpinner = findViewById(R.id.bloodType);
        submitBtn = findViewById(R.id.submitBtn);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        submitBtn.setOnClickListener(v -> {
            Donor donor = new Donor(
                    nameInput.getText().toString(),
                    phoneInput.getText().toString(),
                    bloodSpinner.getSelectedItem().toString(),
                    locationInput.getText().toString(),
                    emailInput.getText().toString(),
                    dateInput.getText().toString()
            );

            apiService.createDonor(donor).enqueue(new Callback<Donor>() {
                @Override
                public void onResponse(Call<Donor> call, Response<Donor> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DonorFormActivity.this, "Donneur enregistré !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DonorFormActivity.this, "Erreur: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Donor> call, Throwable t) {
                    Toast.makeText(DonorFormActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

