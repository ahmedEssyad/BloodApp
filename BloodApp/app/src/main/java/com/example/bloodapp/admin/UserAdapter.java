package com.example.bloodapp.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodapp.R;
import com.example.bloodapp.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onToggleRole(User user);
        void onDeleteUser(User user);
    }

    public UserAdapter(List<User> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole, tvBloodGroup;
        ImageButton btnToggleRole, btnDelete;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            btnToggleRole = itemView.findViewById(R.id.btnToggleRole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(User user) {
            tvName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            tvRole.setText(user.isAdmin() ? "Admin" : "Utilisateur");
            tvBloodGroup.setText(user.getBloodGroup());

            btnToggleRole.setOnClickListener(v -> listener.onToggleRole(user));
            btnDelete.setOnClickListener(v -> listener.onDeleteUser(user));
        }
    }
}
