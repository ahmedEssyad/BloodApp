package com.example.bloodapp.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.models.DonationHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonationHistoryAdapter extends RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder> {

    private List<DonationHistory> donations;
    private SimpleDateFormat dateFormat;

    public DonationHistoryAdapter(List<DonationHistory> donations) {
        this.donations = donations;
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationHistory donation = donations.get(position);
        holder.bind(donation);
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public void updateDonations(List<DonationHistory> newDonations) {
        this.donations = newDonations;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvLocation, tvUnits, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        void bind(DonationHistory donation) {
            // Formater la date
            try {
                Date date = new Date(donation.getDonationDate());
                tvDate.setText(dateFormat.format(date));
            } catch (Exception e) {
                tvDate.setText(donation.getDonationDate());
            }

            tvLocation.setText(donation.getLocation());
            tvUnits.setText(donation.getUnitsdonated() + " unité(s)");
            tvStatus.setText(getStatusText(donation.getStatus()));
        }

        private String getStatusText(String status) {
            switch (status) {
                case "completed":
                    return "Complété";
                case "scheduled":
                    return "Programmé";
                case "cancelled":
                    return "Annulé";
                default:
                    return status;
            }
        }
    }
}
