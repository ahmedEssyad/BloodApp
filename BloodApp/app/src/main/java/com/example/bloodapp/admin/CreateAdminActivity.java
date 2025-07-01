package com.example.bloodapp.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodapp.R;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.AdminCreateRequest;
import com.example.bloodapp.models.User;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAdminActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPassword, etPhone;
    private Button btnCreate;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        setTitle("Créer un administrateur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnCreate = findViewById(R.id.btnCreate);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnCreate.setOnClickListener(v -> {
            if (validateInputs()) {
                createAdmin();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etFullName.getText())) {
            etFullName.setError("Nom requis");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email requis");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Mot de passe requis");
            return false;
        }
        if (etPassword.getText().toString().length() < 8) {
            etPassword.setError("Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Téléphone requis");
            return false;
        }
        return true;
    }

    private void createAdmin() {
        progressBar.setVisibility(View.VISIBLE);
        btnCreate.setEnabled(false);

        AdminCreateRequest request = new AdminCreateRequest(
            etFullName.getText().toString(),
            etEmail.getText().toString(),
            etPassword.getText().toString(),
            etPhone.getText().toString()
        );

        String token = "Bearer " + sessionManager.getToken();

        apiService.createAdmin(token, request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.GONE);
                btnCreate.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(CreateAdminActivity.this, 
                        "Administrateur créé avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateAdminActivity.this, 
                        "Erreur lors de la création", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnCreate.setEnabled(true);
                Toast.makeText(CreateAdminActivity.this, 
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
