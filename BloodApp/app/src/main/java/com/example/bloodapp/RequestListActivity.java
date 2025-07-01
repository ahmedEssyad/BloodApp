package com.example.bloodapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bloodapp.models.BloodRequest;
import java.util.ArrayList;
import java.util.List;

public class RequestListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BloodRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        recyclerView = findViewById(R.id.requestRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ➕ Création de données statiques (exemples de demandes de sang)
        List<BloodRequest> staticRequests = new ArrayList<>();

        // Création de contacts
        BloodRequest.ContactPerson contact1 = new BloodRequest.ContactPerson("Mohamed", "22223344");
        BloodRequest.ContactPerson contact2 = new BloodRequest.ContactPerson("Aminata", "33445566");
        BloodRequest.ContactPerson contact3 = new BloodRequest.ContactPerson("Alioune", "11224488");

        // Ajout des demandes avec tous les champs nécessaires
        staticRequests.add(new BloodRequest("O+", "Nouakchott", 2, "2025-04-23", contact1));
        staticRequests.add(new BloodRequest("A-", "Kiffa", 3, "2025-04-21", contact2));
        staticRequests.add(new BloodRequest("B+", "Nouadhibou", 1, "2025-04-20", contact3));

        // ➕ Initialisation de l'adaptateur avec les données
        adapter = new BloodRequestAdapter(staticRequests);
        recyclerView.setAdapter(adapter);
    }
}
