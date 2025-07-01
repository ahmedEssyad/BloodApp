package com.example.bloodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bloodapp.models.BloodRequest;
import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {

    private List<BloodRequest> requests;

    public BloodRequestAdapter(List<BloodRequest> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodRequest r = requests.get(position);
        holder.hospitalText.setText("Hôpital : " + r.getHospital());
        holder.bloodTypeText.setText("Groupe sanguin : " + r.getBloodType());
        holder.unitsText.setText("Unités : " + r.getUnitsNeeded());
        holder.urgencyText.setText("Urgence : " + r.getUrgencyLevel());
        holder.contactText.setText("Contact : " + r.getContactPerson().getName() + " - " + r.getContactPerson().getPhone());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalText, bloodTypeText, unitsText, urgencyText, contactText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalText = itemView.findViewById(R.id.hospitalText);
            bloodTypeText = itemView.findViewById(R.id.bloodTypeText);
            unitsText = itemView.findViewById(R.id.unitsText);
            urgencyText = itemView.findViewById(R.id.urgencyText);
            contactText = itemView.findViewById(R.id.contactText);
        }
    }
}
