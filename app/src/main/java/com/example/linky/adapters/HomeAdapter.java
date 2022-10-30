package com.example.linky.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linky.R;
import com.example.linky.backend.models.EditableLink;
import com.makeramen.roundedimageview.RoundedImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    EditableLink[] editableLinks;
    Context context;

    public HomeAdapter(Context ct, EditableLink[] editableLinks) {
        context = ct;
        this.editableLinks = editableLinks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_editable_link, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.platformLogo.setImageDrawable(
                ResourcesCompat.getDrawable(
                        context.getResources(),
                        getDrawableByName(editableLinks[position].getPlatform()),
                        null
                )
        );
        holder.platformName.setText(editableLinks[position].getPlatform());
    }

    private int getDrawableByName(String name) {
        switch (name) {
            case "bereal":
                return R.drawable.bereal;
            case "discord":
                return R.drawable.discord;
            case "facebook":
                return R.drawable.facebook;
            case "gmail":
                return R.drawable.gmail;
            case "instagram":
                return R.drawable.instagram;
            case "linkedin":
                return R.drawable.linkedin;
            case "onlyfans":
                return R.drawable.onlyfans;
            case "pinterest":
                return R.drawable.pinterest;
            case "quora":
                return R.drawable.quora;
            case "reddit":
                return R.drawable.reddit;
            case "snapchat":
                return R.drawable.snapchat;
            case "tiktok":
                return R.drawable.tiktok;
            case "twitter":
                return R.drawable.twitter;
            case "yahoo":
                return R.drawable.yahoo;
            default:
                return R.drawable.image_placeholder;
        }
    }

    @Override
    public int getItemCount() {
        return editableLinks.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView platformLogo;
        TextView platformName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.platformLogo) == null)
                Log.e("ERROR", "Logo is null");
            if (itemView.findViewById(R.id.platformName) == null)
                Log.e("ERROR", "Name is null");
            platformLogo = itemView.findViewById(R.id.platformLogo);
            platformName = itemView.findViewById(R.id.platformName);
        }
    }
}
