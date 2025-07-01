package com.example.bloodapp.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.auth.SessionManager;
import com.example.bloodapp.models.User;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUsersActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        setTitle("Gestion des utilisateurs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        String token = "Bearer " + sessionManager.getToken();

        apiService.getAllUsers(token).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateUsers(response.body());
                } else {
                    Toast.makeText(ManageUsersActivity.this, 
                        "Erreur lors du chargement des utilisateurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ManageUsersActivity.this, 
                    "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onToggleRole(User user) {
        String newRole = user.isAdmin() ? "user" : "admin";
        RoleUpdateRequest request = new RoleUpdateRequest(newRole);
        String token = "Bearer " + sessionManager.getToken();

        apiService.updateUserRole(token, user.getId(), request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageUsersActivity.this, 
                        "Rôle modifié avec succès", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Recharger la liste
                } else {
                    Toast.makeText(ManageUsersActivity.this, 
                        "Erreur lors de la modification du rôle", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ManageUsersActivity.this, 
                    "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteUser(User user) {
        // Afficher une dialog de confirmation
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")
            .setPositiveButton("Oui", (dialog, which) -> deleteUser(user))
            .setNegativeButton("Non", null)
            .show();
    }

    private void deleteUser(User user) {
        String token = "Bearer " + sessionManager.getToken();

        apiService.deleteUser(token, user.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageUsersActivity.this, 
                        "Utilisateur supprimé avec succès", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Recharger la liste
                } else {
                    Toast.makeText(ManageUsersActivity.this, 
                        "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageUsersActivity.this, 
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
