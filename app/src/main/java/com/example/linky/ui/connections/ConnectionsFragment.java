package com.example.linky.ui.connections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.adapters.ConnectionsAdapter;
import com.example.linky.backend.models.Connection;
import com.example.linky.databinding.FragmentConnectionsBinding;

import java.util.UUID;

public class ConnectionsFragment extends Fragment {

    private FragmentConnectionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConnectionsViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ConnectionsViewModel.class);

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);

        Connection[] connections = new Connection[] {
                new Connection("Mircea Feder", "federmircea@gmail.com", UUID.randomUUID().toString(), "image"),
                new Connection("Laur Stefoni", "rage.quit@gmail.com", UUID.randomUUID().toString(), "image"),
                new Connection("Cristina Florea", "acristinaflorea@gmail.com", UUID.randomUUID().toString(), "image")
        };

        ConnectionsAdapter adapter = new ConnectionsAdapter(this.getContext(), connections);
        binding.myConnectionsList.setAdapter(adapter);
        binding.myConnectionsList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}