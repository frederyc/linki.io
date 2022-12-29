package com.example.linky.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.R;
import com.example.linky.adapters.HomeAdapter;
import com.example.linky.backend.cache.UserDataCache;
import com.example.linky.backend.interfaces.IEditableLinkClickEvent;
import com.example.linky.backend.models.EditableLink;
import com.example.linky.databinding.FragmentProfileBinding;
import com.example.linky.ui.dialogs.EditLinkDialog;
import com.example.linky.ui.dialogs.LoadingScreen;

public class ProfileFragment extends Fragment implements IEditableLinkClickEvent {
    private FragmentProfileBinding binding;
    private EditableLink[] editableLinks;
    private String fullName, email;
    private Dialog editLink;
    private final DisplayMetrics metrics = new DisplayMetrics();
    private EditLinkDialog editLinkDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("PROFILE_FRAGMENT", "On create view");
        ProfileViewModel homeViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
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

        if (editableLinks == null) {
            LoadingScreen ls = new LoadingScreen(getActivity(), R.layout.loading_screen);
            ls.start();

            UserDataCache.loadCacheAsync((Object... objects) -> {
                this.editableLinks = (EditableLink[]) objects[0];
                this.fullName = (String) objects[1];
                this.email = (String) objects[2];

                binding.name.setText(fullName);
                binding.email.setText(email);

                reloadLinksRecyclerView();

                ls.stop();
            }, (Object... objects) -> {
                ls.stop();
            });
        }
        else {
            binding.name.setText(fullName);
            binding.email.setText(email);

            reloadLinksRecyclerView();
        }

        editLink = new Dialog(getContext());
        editLink.setContentView(R.layout.edit_link_dialog);
        editLink.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        editLink.getWindow().setLayout(
                (int) (metrics.widthPixels * 0.95),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        binding.buttonShareMe.setOnClickListener(view -> handleShareMe());

        return binding.getRoot();
    }

    // TODO: change this with appropriate functionality when reaching the stage
    private void handleShareMe() {
        editLink.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos) {
        Log.d("PROFILE_FRAGMENT", editableLinks[pos].getPlatform());
        editLinkDialog.show(editableLinks[pos]);
    }

    private void reloadLinksRecyclerView() {
        HomeAdapter adapter = new HomeAdapter(this.getContext(), editableLinks, this);
        binding.myLinksList.setAdapter(adapter);
        binding.myLinksList.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}