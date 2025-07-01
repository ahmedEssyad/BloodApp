package com.example.bloodapp.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.models.BloodRequest;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {

    private List<BloodRequest> requests;
    private OnRequestClickListener listener;

    public interface OnRequestClickListener {
        void onRequestClick(BloodRequest request);
    }

    public BloodRequestAdapter(List<BloodRequest> requests, OnRequestClickListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blood_request_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodRequest request = requests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void updateRequests(List<BloodRequest> newRequests) {
        this.requests = newRequests;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospital, tvBloodType, tvUnits, tvUrgency, tvLocation;
        Button btnRespond;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvBloodType = itemView.findViewById(R.id.tvBloodType);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvUrgency = itemView.findViewById(R.id.tvUrgency);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnRespond = itemView.findViewById(R.id.btnRespond);
        }

        void bind(BloodRequest request) {
            tvHospital.setText(request.getHospitalName());
            tvBloodType.setText(request.getBloodType());
            tvUnits.setText(request.getUnitsNeeded() + " unités");
            tvUrgency.setText(getUrgencyText(request.getUrgencyLevel()));
            tvLocation.setText(request.getLocation());

            btnRespond.setOnClickListener(v -> listener.onRequestClick(request));
        }

        private String getUrgencyText(String urgencyLevel) {
            switch (urgencyLevel) {
                case "high":
                    return "Urgent";
                case "medium":
                    return "Moyen";
                case "low":
                    return "Normal";
                default:
                    return urgencyLevel;
            }
        }
    }
}
