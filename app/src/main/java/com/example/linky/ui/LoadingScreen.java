package com.example.linky.ui;

import android.app.Activity;
import android.app.AlertDialog;

public class LoadingScreen {
    private final Activity activity;
    private final int resource;
    private AlertDialog dialog;

    public LoadingScreen(Activity activity, int resource) {
        this.activity = activity;
        this.resource = resource;
    }

    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(activity.getLayoutInflater().inflate(resource, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void stop() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
