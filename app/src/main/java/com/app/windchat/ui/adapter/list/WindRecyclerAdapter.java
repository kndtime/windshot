package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.ui.activity.ShowTImeActivity;
import com.app.windchat.ui.custom.picasso.GrayscaleTransformation;
import com.app.windchat.ui.view.WindViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by banal_a on 08/12/2016.
 */

public class WindRecyclerAdapter extends RecyclerView.Adapter<WindViewHolder> {

    private ArrayList<User> items;
    private Context context;
    private Inflater inflater;

    public WindRecyclerAdapter(Context context, ArrayList<User> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public WindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wind_media_item_layout, parent, false);
        return new WindViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WindViewHolder holder, int position) {
        final User item = getItem(holder.getAdapterPosition());
        holder.getM_container().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpeneable(item)) {
                    Utils.animateError(context, holder.getM_container());
                    return;
                }
                Intent i = new Intent(context, ShowTImeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Snap.setTmpUser(item);
                context.startActivity(i);
            }
        });
        String time = "";
        if (item.getWinds().get(0).isOpened()) {
            Picasso.with(context)
                    .load(item.getPictureUrl())
                    .fit().centerCrop()
                    .transform(new GrayscaleTransformation(Picasso.with(context)))
                    .into(holder.getM_type());
            time = "Seen - Received " + Utils.getTimeSpan(item.getWinds().get(0).getSendDate());
            holder.getM_date().setTypeface(null, Typeface.NORMAL);
        }else {
            Picasso.with(context)
                    .load(item.getPictureUrl())
                    .fit().centerCrop()
                    .into(holder.getM_type());
            time = "Received " + Utils.getTimeSpan(item.getWinds().get(0).getSendDate());
            holder.getM_date().setTypeface(null, Typeface.BOLD);
        }

        holder.getM_date().setText(time);
        holder.getM_name().setText(item.getCompleteName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private User getItem(int position) {
        return items.get(position);
    }

    public void add(User item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public void addAll(List<User> videos) {
        for (User user : videos) {
            add(user);
        }
    }

    public void clearAll(){
        items.clear();
        notifyDataSetChanged();
    }

    private boolean isOpeneable(User user){
        boolean open = false;
        for (Wind wi :
                user.getWinds()) {
            open = open || !wi.isOpened();
        }
        return open;
    }
}
