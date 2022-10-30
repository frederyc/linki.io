package com.example.linky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linky.R;
import com.example.linky.backend.models.Connection;
import com.makeramen.roundedimageview.RoundedImageView;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.MyViewHolder> {
    Context ct;
    Connection[] connections;

    public ConnectionsAdapter(Context ct, Connection[] connections) {
        this.ct = ct;
        this.connections = connections;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.user_connections_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userIcon.setImageDrawable(
                ResourcesCompat.getDrawable(
                        ct.getResources(),
                        R.drawable.profile_placeholder,
                        null
                )
        );
        holder.userName.setText(connections[position].getName());
        holder.userEmail.setText(connections[position].getEmail());
    }

    @Override
    public int getItemCount() {
        return connections.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView userIcon;
        TextView userName, userEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.userIcon);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
        }
    }
}
