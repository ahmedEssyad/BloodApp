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
import com.example.bloodapp.models.PasswordChangeRequest;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChange;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setTitle("Changer le mot de passe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChange = findViewById(R.id.btnChange);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnChange.setOnClickListener(v -> {
            if (validateInputs()) {
                changePassword();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etCurrentPassword.getText())) {
            etCurrentPassword.setError("Mot de passe actuel requis");
            return false;
        }
        if (TextUtils.isEmpty(etNewPassword.getText())) {
            etNewPassword.setError("Nouveau mot de passe requis");
            return false;
        }
        if (etNewPassword.getText().toString().length() < 8) {
            etNewPassword.setError("Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }
        if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Les mots de passe ne correspondent pas");
            return false;
        }
        return true;
    }

    private void changePassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnChange.setEnabled(false);

        PasswordChangeRequest request = new PasswordChangeRequest(
            etCurrentPassword.getText().toString(),
            etNewPassword.getText().toString()
        );

        String token = "Bearer " + sessionManager.getToken();

        apiService.changeAdminPassword(token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                btnChange.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, 
                        "Mot de passe modifié avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, 
                        "Échec de la modification", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnChange.setEnabled(true);
                Toast.makeText(ChangePasswordActivity.this, 
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
