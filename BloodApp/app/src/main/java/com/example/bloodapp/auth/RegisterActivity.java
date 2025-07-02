package com.example.bloodapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bloodapp.MainActivity;
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

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone, etAge, etPassword, etConfirmPassword, etLocation;
    private AutoCompleteTextView acBloodGroup;
    private CheckBox cbTerms;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser SessionManager
        sessionManager = new SessionManager(this);
        
        // Si l'utilisateur est déjà connecté, rediriger vers MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        // Initialiser l'API
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Référencer les vues
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        acBloodGroup = findViewById(R.id.acBloodGroup);
        etAge = findViewById(R.id.etAge);
        etLocation = findViewById(R.id.etLocation);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Configuration du dropdown pour les groupes sanguins
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, bloodGroups);
        acBloodGroup.setAdapter(adapter);

        // Configuration des couleurs du bouton
        btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        // Gérer le clic sur le bouton d'inscription
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        // Gérer le clic pour aller à l'écran de connexion
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Validation des champs de saisie
    private boolean validateInputs() {
        boolean isValid = true;

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String bloodGroup = acBloodGroup.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        boolean termsAccepted = cbTerms.isChecked();

        // Validation du nom complet
        if (TextUtils.isEmpty(fullName)) {
            setInputError(etFullName, "Veuillez entrer votre nom complet");
            isValid = false;
        } else {
            clearInputError(etFullName);
        }

        // Validation de l'email
        if (TextUtils.isEmpty(email)) {
            setInputError(etEmail, "Veuillez entrer votre email");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setInputError(etEmail, "Veuillez entrer un email valide");
            isValid = false;
        } else {
            clearInputError(etEmail);
        }

        // Validation du téléphone
        if (TextUtils.isEmpty(phone)) {
            setInputError(etPhone, "Veuillez entrer votre numéro de téléphone");
            isValid = false;
        } else if (phone.length() < 8 || phone.length() > 15) {
            setInputError(etPhone, "Numéro de téléphone invalide");
            isValid = false;
        } else {
            clearInputError(etPhone);
        }

        // Validation du groupe sanguin
        if (TextUtils.isEmpty(bloodGroup)) {
            acBloodGroup.setError("Veuillez sélectionner votre groupe sanguin");
            isValid = false;
        } else {
            acBloodGroup.setError(null);
        }

        // Validation de l'âge
        if (TextUtils.isEmpty(age)) {
            setInputError(etAge, "Veuillez entrer votre âge");
            isValid = false;
        } else {
            try {
                int ageValue = Integer.parseInt(age);
                if (ageValue < 18 || ageValue > 65) {
                    setInputError(etAge, "Vous devez avoir entre 18 et 65 ans");
                    isValid = false;
                } else {
                    clearInputError(etAge);
                }
            } catch (NumberFormatException e) {
                setInputError(etAge, "Veuillez entrer un âge valide");
                isValid = false;
            }
        }

        // Validation de la localisation
        if (TextUtils.isEmpty(location)) {
            setInputError(etLocation, "Veuillez entrer votre ville ou quartier");
            isValid = false;
        } else {
            clearInputError(etLocation);
        }

        // Validation du mot de passe
        if (TextUtils.isEmpty(password)) {
            setInputError(etPassword, "Veuillez entrer un mot de passe");
            isValid = false;
        } else if (password.length() < 6) {
            setInputError(etPassword, "Le mot de passe doit contenir au moins 6 caractères");
            isValid = false;
        } else {
            clearInputError(etPassword);
        }

        // Validation de la confirmation du mot de passe
        if (!password.equals(confirmPassword)) {
            setInputError(etConfirmPassword, "Les mots de passe ne correspondent pas");
            isValid = false;
        } else {
            clearInputError(etConfirmPassword);
        }

        // Validation des termes et conditions
        if (!termsAccepted) {
            Toast.makeText(this, "Veuillez accepter les termes et conditions", Toast.LENGTH_SHORT).show();
            isValid = false;
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

    // Enregistrement de l'utilisateur
    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String bloodGroup = acBloodGroup.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
        String location = etLocation.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Créer la demande d'inscription
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName(fullName);
        registerRequest.setEmail(email);
        registerRequest.setPhone(phone);
        registerRequest.setBloodGroup(bloodGroup);
        registerRequest.setAge(age);
        registerRequest.setLocation(location);
        registerRequest.setPassword(password);

        // Désactiver le bouton et afficher un message de chargement
        btnRegister.setEnabled(false);
        btnRegister.setText("Inscription en cours...");

        // Envoyer la demande au serveur
        Call<AuthResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText("S'inscrire");

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    
                    // Enregistrer les données de l'utilisateur
                    sessionManager.saveAuthToken(authResponse.getToken());
                    sessionManager.saveUserDetails(fullName, email, bloodGroup);
                    
                    // Afficher un message de succès
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    
                    // Rediriger vers l'activité principale
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Afficher un message d'erreur
                    String errorMessage = ErrorHandler.parseError(response);
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("S'inscrire");
                
                // Enhanced error logging
                Log.e("REGISTER_ERROR", "Registration failed", t);
                Log.e("REGISTER_ERROR", "Error type: " + t.getClass().getSimpleName());
                Log.e("REGISTER_ERROR", "Error message: " + t.getMessage());
                
                String errorMessage = ErrorHandler.getErrorMessage(t);
                Toast.makeText(RegisterActivity.this, "Erreur réseau: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Retourner à l'écran de connexion
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
