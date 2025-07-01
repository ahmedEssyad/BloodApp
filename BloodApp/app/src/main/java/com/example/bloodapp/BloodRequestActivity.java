package com.example.bloodapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bloodapp.models.BloodRequest;
import com.example.bloodapp.models.BloodRequest.ContactPerson;
import com.example.bloodapp.network.ApiService;
import com.example.bloodapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodRequestActivity extends AppCompatActivity {

    EditText hospitalInput, unitsInput, contactNameInput, contactPhoneInput;
    Spinner bloodTypeSpinner, urgencySpinner;
    Button submitBtn;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        hospitalInput = findViewById(R.id.hospital);
        unitsInput = findViewById(R.id.units);
        contactNameInput = findViewById(R.id.contactName);
        contactPhoneInput = findViewById(R.id.contactPhone);
        bloodTypeSpinner = findViewById(R.id.bloodType);
        urgencySpinner = findViewById(R.id.urgency);
        submitBtn = findViewById(R.id.submitBtn);

        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        String[] urgencyLevels = {"low", "medium", "high", "critical"};

        bloodTypeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodTypes));
        urgencySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, urgencyLevels));

        apiService = RetrofitClient.getClient().create(ApiService.class);

        submitBtn.setOnClickListener(v -> {
            String hospital = hospitalInput.getText().toString();
            String bloodType = bloodTypeSpinner.getSelectedItem().toString();
            int units = Integer.parseInt(unitsInput.getText().toString());
            String urgency = urgencySpinner.getSelectedItem().toString();
            String contactName = contactNameInput.getText().toString();
            String contactPhone = contactPhoneInput.getText().toString();

            BloodRequest request = new BloodRequest(hospital, bloodType, units, urgency, new ContactPerson(contactName, contactPhone));

            apiService.createBloodRequest(request).enqueue(new Callback<BloodRequest>() {
                @Override
                public void onResponse(Call<BloodRequest> call, Response<BloodRequest> response) {
                    Toast.makeText(BloodRequestActivity.this, "Demande envoyée avec succès", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BloodRequest> call, Throwable t) {
                    Toast.makeText(BloodRequestActivity.this, "Échec : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
