package com.example.linky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linky.R;
import com.example.linky.backend.interfaces.IPlatformLinkClickEvent;
import com.example.linky.backend.models.Connection;

import java.util.List;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.MyViewHolder> {
    private final IPlatformLinkClickEvent platformLinkClickEvent;
    Context ct;
    List<Connection> connections;

    public ConnectionsAdapter(
            Context ct,
            List<Connection> connections,
            IPlatformLinkClickEvent platformLinkClickEvent
    ) {
        this.ct = ct;
        this.connections = connections;
        this.platformLinkClickEvent = platformLinkClickEvent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.user_connections_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(connections.get(position).getName());
        holder.userEmail.setText(connections.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UCCFullName);
            userEmail = itemView.findViewById(R.id.UCCEmail);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    platformLinkClickEvent.onItemClick(pos);
            });
        }
    }
}
