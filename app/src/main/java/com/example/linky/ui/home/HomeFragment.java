package com.example.linky.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.linky.HomeAdapter;
import com.example.linky.backend.models.EditableLink;
import com.example.linky.databinding.FragmentHomeBinding;

import java.util.UUID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}