package com.example.bloodapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bloodapp.MainActivity;
import com.example.bloodapp.admin.AdminDashboardActivity;
import com.example.bloodapp.R;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;
import com.example.bloodapp.models.User;
import com.example.bloodapp.utils.ErrorHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser SessionManager
        sessionManager = new SessionManager(this);
        
        // Si l'utilisateur est déjà connecté, rediriger vers MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // Initialiser l'API
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Référencer les vues
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Configuration des couleurs du bouton
        btnLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        // Gérer le clic sur le bouton de connexion
        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                loginUser();
            }
        });

        // Gérer le clic pour aller à l'écran d'inscription
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Gérer le clic pour mot de passe oublié
        tvForgotPassword.setOnClickListener(v -> {
            // TODO: Implémenter la fonctionnalité de récupération de mot de passe
            Toast.makeText(LoginActivity.this, "Fonctionnalité à venir", Toast.LENGTH_SHORT).show();
        });
    }

    // Validation des champs de saisie
    private boolean validateInputs() {
        boolean isValid = true;

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            setInputError(etEmail, "Veuillez entrer votre email");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setInputError(etEmail, "Veuillez entrer un email valide");
            isValid = false;
        } else {
            clearInputError(etEmail);
        }

        if (TextUtils.isEmpty(password)) {
            setInputError(etPassword, "Veuillez entrer votre mot de passe");
            isValid = false;
        } else {
            clearInputError(etPassword);
        }

        return isValid;
    }

    // Helper method pour afficher une erreur sur un champ
    private void setInputError(TextInputEditText editText, String error) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        textInputLayout.setError(error);
    }

    // Helper method pour effacer une erreur sur un champ
    private void clearInputError(TextInputEditText editText) {
        TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
        textInputLayout.setError(null);
    }

    // Connexion de l'utilisateur
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Créer la demande de connexion
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(email);
        authRequest.setPassword(password);

        // Désactiver le bouton et afficher un message de chargement
        btnLogin.setEnabled(false);
        btnLogin.setText("Connexion en cours...");

        // Envoyer la demande au serveur
        Call<AuthResponse> call = apiService.login(authRequest);
        call.enqueue(new LoginCallback());
    }
    
    private class LoginCallback implements Callback<AuthResponse> {
        @Override
        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
            btnLogin.setEnabled(true);
            btnLogin.setText("Se connecter");

            if (response.isSuccessful() && response.body() != null) {
                AuthResponse authResponse = response.body();
                
                // Enregistrer le token et les informations utilisateur
                sessionManager.saveAuthToken(authResponse.getToken());
                sessionManager.saveUserDetails(authResponse.getUser().getFullName(), 
                                              authResponse.getUser().getEmail(), 
                                              authResponse.getUser().getBloodGroup());
                
                // Afficher un message de succès
                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                
                // Rediriger vers l'activité appropriée selon le rôle
                Intent intent;
                if (authResponse.getUser() != null && authResponse.getUser().isAdmin()) {
                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // Afficher un message d'erreur
                String errorMessage = ErrorHandler.parseError(response);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<AuthResponse> call, Throwable t) {
            btnLogin.setEnabled(true);
            btnLogin.setText("Se connecter");
            String errorMessage = ErrorHandler.getErrorMessage(t);
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
