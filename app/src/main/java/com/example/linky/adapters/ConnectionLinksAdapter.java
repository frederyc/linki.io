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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectionLinksAdapter
        extends RecyclerView.Adapter<ConnectionLinksAdapter.MyViewHolder> {

    private final IPlatformLinkClickEvent platformLinkClickEvent;
    List<PlatformLink> platformLinks;
    Context context;

    public ConnectionLinksAdapter(
            Context ct,
            List<PlatformLink> platformLinks,
            IPlatformLinkClickEvent platformLinkClickEvent
    ) {
        this.platformLinkClickEvent = platformLinkClickEvent;
        this.platformLinks = platformLinks;
        this.context = ct;
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
                        getDrawableByName(platformLinks.get(position).getPlatform()),
                        null
                )
        );
        holder.platformName.setText(platformLinks.get(position).getPlatform());
        holder.edit.setVisibility(View.GONE);
        holder.add.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return platformLinks.size();
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
                if (pos != RecyclerView.NO_POSITION) {
                    platformLinkClickEvent.onItemClick(pos);
                }
            });
        }
    }
}
