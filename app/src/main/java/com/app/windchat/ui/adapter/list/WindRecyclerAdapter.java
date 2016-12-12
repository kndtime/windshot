package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.windchat.R;
import com.app.windchat.Utils;
import com.app.windchat.api.model.Wind;
import com.app.windchat.ui.view.WindViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by banal_a on 08/12/2016.
 */

public class WindRecyclerAdapter extends RecyclerView.Adapter<WindViewHolder> {

    private ArrayList<Wind> items;
    private Context context;
    private Inflater inflater;

    public WindRecyclerAdapter(Context context, ArrayList<Wind> items) {
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
    public void onBindViewHolder(WindViewHolder holder, int position) {
        Wind item = getItem(holder.getAdapterPosition());
        holder.getM_date().setText(Utils.getTimeSpan(item.getSendDate()));
        holder.getM_name().setText(item.getUser().getCompleteName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Wind getItem(int position) {
        return items.get(position);
    }

    private void add(Wind item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    public void addAll(List<Wind> videos) {
        for (Wind user : videos) {
            add(user);
        }
    }

    public void clearAll(){
        items.clear();
        notifyDataSetChanged();
    }
}
