package com.example.linky.ui.connections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.ConnectionDetailsActivity;
import com.example.linky.R;
import com.example.linky.adapters.ConnectionsAdapter;
import com.example.linky.backend.cache.ConnectionsDataCache;
import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.interfaces.IPlatformLinkClickEvent;
import com.example.linky.backend.models.Connection;
import com.example.linky.databinding.FragmentConnectionsBinding;
import com.example.linky.ui.dialogs.LoadingScreen;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsFragment extends Fragment implements IPlatformLinkClickEvent {
    private FragmentConnectionsBinding binding;
    private List<Connection> connections;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("CONNECTIONS_FRAGMENT", "created view");

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        connections = new ArrayList<>();

        LoadingScreen ls = new LoadingScreen(getActivity(), R.layout.loading_screen);
        ls.start();

        ConnectionsDataCache.loadDataAsync(new ArrayList<>(UserDataCache.getConnections()),
                (Object... objects) -> {
                    connections.addAll(ConnectionsDataCache.getConnectionsData().values());
                    setUIData(connections);
                    ls.stop();
                },
                (Object... objects) -> {
                    ls.stop();
                    setUIData(new ArrayList<>());
                    Toast.makeText(
                            getContext(),
                            "Error occurred when loading connections",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        return binding.getRoot();
    }

    public void setUIData(List<Connection> connections) {
        binding.FCNoConnections.setVisibility(connections.isEmpty() ? View.VISIBLE : View.GONE);
        ConnectionsAdapter adapter = new ConnectionsAdapter(this.getContext(), connections, this);
        binding.FCConnectionsList.setAdapter(adapter);
        binding.FCConnectionsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos) {
        Intent navIntent = new Intent(getActivity(), ConnectionDetailsActivity.class);
        navIntent.putExtra("uuid", connections.get(pos).getUuid());
        startActivity(navIntent);
    }
}