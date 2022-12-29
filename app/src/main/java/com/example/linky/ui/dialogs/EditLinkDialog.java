package com.example.linky.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.linky.R;
import com.example.linky.backend.interfaces.ILambda;
import com.example.linky.backend.models.EditableLink;
import com.example.linky.backend.services.LinkValidationService;
import com.example.linky.backend.services.UserDataService;

public class EditLinkDialog {
    private final Dialog dialog;
    private final UserDataService userDataService;
    private final LinkValidationService linkValidationService;

    private final Activity activity;
    private final ImageView platformLogo;
    private final Button updateLink, deleteLink;
    private final TextView platformName;
    private final EditText link;
    private final ILambda updateRecyclerView;       // call when link is updates/deleted

    public EditLinkDialog(
            Context ct,
            DisplayMetrics dm,
            Activity ac,
            ILambda urv
    ) {
        dialog = new Dialog(ct);
        dialog.setContentView(R.layout.edit_link_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(
                (int) (dm.widthPixels * 0.95),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        activity = ac;
        updateRecyclerView = urv;

        userDataService = UserDataService.getInstance();
        linkValidationService = LinkValidationService.getInstance();

        platformLogo = (ImageView) dialog.findViewById(R.id.EDLPlatformLogo);
        platformName = (TextView) dialog.findViewById(R.id.EDLPLatformName);
        updateLink = (Button) dialog.findViewById(R.id.EDLUpdateLink);
        deleteLink = (Button) dialog.findViewById(R.id.EDLDeleteLink);
        link = (EditText) dialog.findViewById(R.id.EDLLink);
    }

    private void handleUpdateLink(EditableLink el) {
        String link = ((EditText) dialog.findViewById(R.id.EDLLink)).getText().toString();

        if (!linkValidationService.checkLink(el.getPlatform(), link)) {
            Toast.makeText(dialog.getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingScreen loadingScreen = new LoadingScreen(
                activity,
                R.layout.loading_screen);
        loadingScreen.start();

        userDataService.updateLink(
                new EditableLink(el.getPlatform(), link), (Object... objects) -> {
                    el.setLink(link);
                    updateRecyclerView.call();

                    loadingScreen.stop();
                    dialog.dismiss();
                    Toast.makeText(dialog.getContext(), "Update succeeded", Toast.LENGTH_SHORT)
                            .show();
                }, (Object... objects) -> {
                    loadingScreen.stop();
                    Toast.makeText(dialog.getContext(), "Update failed", Toast.LENGTH_SHORT)
                            .show();
                }
        );
    }

    private void handleDeleteLink(EditableLink el) {
        LoadingScreen loadingScreen = new LoadingScreen(
                activity,
                R.layout.loading_screen);
        loadingScreen.start();

        userDataService.updateLink(
                new EditableLink(el.getPlatform(), ""), (Object... objects) -> {
                    el.setLink("");
                    updateRecyclerView.call();

                    loadingScreen.stop();
                    dialog.dismiss();
                    Toast.makeText(dialog.getContext(), "Update succeeded", Toast.LENGTH_SHORT)
                            .show();
                }, (Object... objects) -> {
                    loadingScreen.stop();
                    Toast.makeText(dialog.getContext(), "Update failed", Toast.LENGTH_SHORT)
                            .show();
                }
        );
    }

    public void show(EditableLink el) {
        platformLogo.setImageDrawable(
                ResourcesCompat.getDrawable(
                dialog.getContext().getResources(),
                getDrawableByName(el.getPlatform()),
                null
        ));
        platformName.setText(el.getPlatform());
        link.setText(el.getLink());
        updateLink.setOnClickListener(view -> handleUpdateLink(el));
        deleteLink.setOnClickListener(view -> handleDeleteLink(el));
        dialog.show();
    }

    private int getDrawableByName(String name) {
        switch (name) {
            case "BeReal":
                return R.drawable.bereal;
            case "Discord":
                return R.drawable.discord;
            case "Facebook":
                return R.drawable.facebook;
            case "GitHub":
                return R.drawable.github;
            case "Gmail":
                return R.drawable.gmail;
            case "Instagram":
                return R.drawable.instagram;
            case "LinkedIn":
                return R.drawable.linkedin;
            case "OnlyFans":
                return R.drawable.onlyfans;
            case "Pinterest":
                return R.drawable.pinterest;
            case "Quora":
                return R.drawable.quora;
            case "Reddit":
                return R.drawable.reddit;
            case "Snapchat":
                return R.drawable.snapchat;
            case "TikTok":
                return R.drawable.tiktok;
            case "Twitter":
                return R.drawable.twitter;
            case "Yahoo":
                return R.drawable.yahoo;
            default:
                return R.drawable.image_placeholder;
        }
    }

}
