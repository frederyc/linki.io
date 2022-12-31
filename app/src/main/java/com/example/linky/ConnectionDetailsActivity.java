package com.example.linky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linky.adapters.ConnectionLinksAdapter;
import com.example.linky.backend.cache.ConnectionsDataCache;
import com.example.linky.backend.interfaces.IPlatformLinkClickEvent;
import com.example.linky.backend.models.Connection;
import com.example.linky.backend.models.PlatformLink;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionDetailsActivity extends AppCompatActivity implements IPlatformLinkClickEvent {
    // no binding, for whatever reason
    private ConstraintLayout layout;
    private RecyclerView linksList;
    private TextView name, email, noLinks;
    private List<PlatformLink> platformLinks;
    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CONNECTION_DETAILS_ACTIVITY", "created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_details);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e("CONNECTION_DETAILS_ACTIVITY", "extras is null");
            return;
        }

        Connection connection = ConnectionsDataCache
                .getConnectionsData()
                .get(extras.getString("uuid"));
        platformLinks = new ArrayList<>();

        layout = findViewById(R.id.CDActivity);
        linksList = findViewById(R.id.CDConnectionLinksList);
        email = findViewById(R.id.CDEmail);
        name = findViewById(R.id.CDName);
        noLinks = findViewById(R.id.CDNoLinks);
        setBackgroundColor();

        assert connection != null;
        setUIData(connection);
    }

    private void setBackgroundColor() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        layout.setBackgroundColor(getResources().getColor(
                nightModeFlags == Configuration.UI_MODE_NIGHT_YES
                        ? R.color.surface_D
                        : R.color.surface_L
        ));
    }

    private void setUIData(Connection connection) {
        name.setText(connection.getName());
        email.setText(connection.getEmail());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            platformLinks.addAll(connection.getPlatformLinks().entrySet().stream()
                    .filter(n -> !n.getValue().isEmpty())
                    .map(n -> new PlatformLink(n.getKey(), n.getValue()))
                    .collect(Collectors.toList())
            );
        else
            Toast.makeText(this, "Minimum sdk needs to be 24", Toast.LENGTH_SHORT).show();

        noLinks.setVisibility(platformLinks.isEmpty() ? View.VISIBLE : View.GONE);

        ConnectionLinksAdapter adapter = new ConnectionLinksAdapter(this, platformLinks, this);

        linksList.setAdapter(adapter);
        linksList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemClick(int pos) {
        if (platformLinks.get(pos).getLink().startsWith("https://")) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(platformLinks.get(pos).getLink()));
            startActivity(webIntent);
        }
        else {
            clipboard.setPrimaryClip(ClipData.newPlainText(
                    String.format("Data for %s", platformLinks.get(pos).getPlatform()),
                    platformLinks.get(pos).getLink())
            );
            Toast.makeText(this, "Data copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }
}