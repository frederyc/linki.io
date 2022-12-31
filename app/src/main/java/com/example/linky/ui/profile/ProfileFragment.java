package com.example.linky.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.QrScannerActivity;
import com.example.linky.R;
import com.example.linky.adapters.ProfileAdapter;
import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.interfaces.IPlatformLinkClickEvent;
import com.example.linky.backend.models.PlatformLink;
import com.example.linky.databinding.FragmentProfileBinding;
import com.example.linky.ui.dialogs.EditLinkDialog;
import com.example.linky.ui.dialogs.LoadingScreen;

public class ProfileFragment extends Fragment implements IPlatformLinkClickEvent {
    private FragmentProfileBinding binding;
    private PlatformLink[] platformLinks;
    private String fullName, email;
    private Dialog editLink;
    private Bitmap qrCode;
    private final DisplayMetrics metrics = new DisplayMetrics();
    private EditLinkDialog editLinkDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("PROFILE_FRAGMENT", "On create view");
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        assert getContext() != null;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        editLinkDialog = new EditLinkDialog(
                getContext(),
                metrics,
                getActivity(),
                (Object... objects) -> reloadLinksRecyclerView()
        );

        if (
                UserDataCache.getEditableLinks() == null ||
                UserDataCache.getConnections() == null ||
                UserDataCache.getFullName() == null ||
                UserDataCache.getEmail() == null ||
                UserDataCache.getQrCode() == null
        ) {
            LoadingScreen ls = new LoadingScreen(getActivity(), R.layout.loading_screen);
            ls.start();

            UserDataCache.loadCacheAsync((Object... objects) -> {
                loadCacheData();

                binding.PFName.setText(fullName);
                binding.PFEmail.setText(email);
                binding.PFQRCode.setImageBitmap(qrCode);

                reloadLinksRecyclerView();

                ls.stop();
            }, (Object... objects) -> {
                ls.stop();
            });
        }
        else {
            binding.PFName.setText(fullName);
            binding.PFEmail.setText(email);
            binding.PFQRCode.setImageBitmap(qrCode);

            if (platformLinks == null || fullName == null || email == null || qrCode == null)
                loadCacheData();

            reloadLinksRecyclerView();
        }

        editLink = new Dialog(getContext());
        editLink.setContentView(R.layout.edit_link_dialog);
        editLink.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        editLink.getWindow().setLayout(
                (int) (metrics.widthPixels * 0.95),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        binding.PFScanQR.setOnClickListener(view -> handleShareMe());

        return binding.getRoot();
    }

    private void handleShareMe() {
        Intent navIntent = new Intent(getActivity(), QrScannerActivity.class);
        startActivity(navIntent);
    }

    private void loadCacheData() {
        this.platformLinks = UserDataCache.getEditableLinks();
        this.fullName = UserDataCache.getFullName();
        this.email = UserDataCache.getEmail();
        this.qrCode = UserDataCache.getQrCode();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos) {
        Log.d("PROFILE_FRAGMENT", platformLinks[pos].getPlatform());
        editLinkDialog.show(platformLinks[pos]);
    }

    private void reloadLinksRecyclerView() {
        ProfileAdapter adapter = new ProfileAdapter(this.getContext(), platformLinks, this);
        binding.PFMyLinksList.setAdapter(adapter);
        binding.PFMyLinksList.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}