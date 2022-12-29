package com.example.linky.adapters;

import android.content.Context;
import android.content.res.Resources;
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
import com.example.linky.backend.interfaces.IEditableLinkClickEvent;
import com.example.linky.backend.models.EditableLink;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private final IEditableLinkClickEvent editableLinkClickEvent;
    EditableLink[] editableLinks;
    Context context;

    public HomeAdapter(
            Context ct,
            EditableLink[] editableLinks,
            IEditableLinkClickEvent editableLinkClickEvent
    ) {
        context = ct;
        this.editableLinks = editableLinks;
        this.editableLinkClickEvent = editableLinkClickEvent;
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

        boolean emptyLink = editableLinks[position].getLink().isEmpty();
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
        return editableLinks.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView platformLogo;
        TextView platformName;
        CircleImageView add, edit;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.UELPlatformLogo) == null)
                Log.e("ERROR", "Logo is null");
            if (itemView.findViewById(R.id.UELPlatformName) == null)
                Log.e("ERROR", "Name is null");
            if (itemView.findViewById(R.id.UELCardView) == null)
                Log.e("ERROR", "CardView is null");
            if (itemView.findViewById(R.id.UELAdd) == null)
                Log.e("ERROR", "Add is null");
            if (itemView.findViewById(R.id.UELEdit) == null)
                Log.e("ERROR", "Edit is null");

            platformLogo = itemView.findViewById(R.id.UELPlatformLogo);
            platformName = itemView.findViewById(R.id.UELPlatformName);
            add = itemView.findViewById(R.id.UELAdd);
            edit = itemView.findViewById(R.id.UELEdit);
            cardView = itemView.findViewById(R.id.UELCardView);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    editableLinkClickEvent.onItemClick(pos);
                }
            });
        }
    }
}
