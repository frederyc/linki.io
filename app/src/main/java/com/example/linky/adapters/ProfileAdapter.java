package com.example.linky.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linky.R;
import com.example.linky.backend.interfaces.IPlatformLinkClickEvent;
import com.example.linky.backend.models.PlatformLink;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private final IPlatformLinkClickEvent platformLinkClickEvent;
    PlatformLink[] platformLinks;
    Context context;

    public ProfileAdapter(
            Context ct,
            PlatformLink[] platformLinks,
            IPlatformLinkClickEvent platformLinkClickEvent
    ) {
        this.context = ct;
        this.platformLinks = platformLinks;
        this.platformLinkClickEvent = platformLinkClickEvent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_platform_link, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.platformLogo.setImageDrawable(
                ResourcesCompat.getDrawable(
                        context.getResources(),
                        getDrawableByName(platformLinks[position].getPlatform()),
                        null
                )
        );
        holder.platformName.setText(platformLinks[position].getPlatform());

        boolean emptyLink = platformLinks[position].getLink().isEmpty();
        holder.edit.setVisibility(emptyLink ? View.GONE : View.VISIBLE);
        holder.add.setVisibility(emptyLink ? View.VISIBLE : View.GONE);
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

    @Override
    public int getItemCount() {
        return platformLinks.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView platformLogo;
        TextView platformName;
        CircleImageView add, edit;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.UPLPlatformLogo) == null)
                Log.e("ERROR", "Logo is null");
            if (itemView.findViewById(R.id.UPLPlatformName) == null)
                Log.e("ERROR", "Name is null");
            if (itemView.findViewById(R.id.UPLCardView) == null)
                Log.e("ERROR", "CardView is null");
            if (itemView.findViewById(R.id.UPLAdd) == null)
                Log.e("ERROR", "Add is null");
            if (itemView.findViewById(R.id.UPLEdit) == null)
                Log.e("ERROR", "Edit is null");

            platformLogo = itemView.findViewById(R.id.UPLPlatformLogo);
            platformName = itemView.findViewById(R.id.UPLPlatformName);
            add = itemView.findViewById(R.id.UPLAdd);
            edit = itemView.findViewById(R.id.UPLEdit);
            cardView = itemView.findViewById(R.id.UPLCardView);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    platformLinkClickEvent.onItemClick(pos);
            });
        }
    }
}
