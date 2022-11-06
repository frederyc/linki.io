package com.example.linky.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.R;
import com.example.linky.adapters.HomeAdapter;
import com.example.linky.backend.models.EditableLink;
import com.example.linky.databinding.FragmentProfileBinding;

import java.util.UUID;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Dialog editLink;
    private DisplayMetrics metrics = new DisplayMetrics();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel homeViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        EditableLink[] editableLinks = new EditableLink[]{
                new EditableLink("facebook", "www.facebook.com/mircea-feder", UUID.randomUUID().toString()),
                new EditableLink("pinterest", "www.facebook.com/mircea-popa", UUID.randomUUID().toString()),
                new EditableLink("bereal", "www.facebook.com/cristina-florea", UUID.randomUUID().toString()),
                new EditableLink("reddit", "www.facebook.com/ligius-matesoni", UUID.randomUUID().toString()),
                new EditableLink("facebook", "www.facebook.com/mircea-feder", UUID.randomUUID().toString()),
                new EditableLink("pinterest", "www.facebook.com/mircea-popa", UUID.randomUUID().toString()),
                new EditableLink("bereal", "www.facebook.com/cristina-florea", UUID.randomUUID().toString()),
                new EditableLink("reddit", "www.facebook.com/ligius-matesoni", UUID.randomUUID().toString()),
                new EditableLink("facebook", "www.facebook.com/mircea-feder", UUID.randomUUID().toString()),
                new EditableLink("pinterest", "www.facebook.com/mircea-popa", UUID.randomUUID().toString()),
                new EditableLink("bereal", "www.facebook.com/cristina-florea", UUID.randomUUID().toString()),
                new EditableLink("reddit", "www.facebook.com/ligius-matesoni", UUID.randomUUID().toString()),
        };

        HomeAdapter adapter = new HomeAdapter(this.getContext(), editableLinks);
        binding.myLinksList.setAdapter(adapter);
        binding.myLinksList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        editLink = new Dialog(getContext());
        editLink.setContentView(R.layout.edit_link_dialog);
        editLink.getWindow().setLayout((int) (metrics.widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);

        binding.buttonShareMe.setOnClickListener(view -> editLink.show());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}