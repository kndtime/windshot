package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.ui.activity.ShowTImeActivity;
import com.app.windchat.ui.view.StoryFooterView;
import com.app.windchat.ui.view.StoryHeaderView;
import com.app.windchat.ui.view.StoryItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banal_a on 18/12/2016.
 */

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private ArrayList<User> users;
    private Context context;

    public StoryAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        users.add(Snap.getCurrent());
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        } else if(isPositionFooter (position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.header_story_item, parent, false);
            return new StoryHeaderView(v);
        } else if(viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.footer_story_item, parent, false);
            return new StoryFooterView(v);
        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_story_item, parent, false);
            return new StoryItemView(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StoryHeaderView){
            final StoryHeaderView hl = (StoryHeaderView) holder;
            if (getItemCount() < 2)
                return;

            Picasso.with(context)
                    .load(users.get(0).getPictureUrl())
                    .fit().centerCrop().into(hl.getM_type());
            hl.getM_name().setText(users.get(0).getCompleteName());

            if (users.get(0).getWinds().size() > 0){
                hl.getM_date().setText(
                        Utils.getTimeSpan(
                                users.get(0).getWinds().get(0).getSendDate()));
            }else{
                hl.getM_date().setText("Post a story");
                Utils.startMainIntent(1);
            }

            if (users.size() > 1)
                hl.getEmpty().setVisibility(View.VISIBLE);
            else
                hl.getEmpty().setVisibility(View.GONE);

            hl.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOpeneable(getItem(position))) {
                        Utils.animateError(context, hl.getContainer());
                        return;
                    }
                    Intent i = new Intent(context, ShowTImeActivity.class);
                    i.putExtra("position", 2);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Snap.setTmpUser(getItem(position));
                    context.startActivity(i);
                }
            });
        }

        if (holder instanceof StoryFooterView){

        }

        if (holder instanceof StoryItemView){
            if (users.size() == 1)
                return;
            final StoryItemView hl = (StoryItemView) holder;
            int pos = holder.getAdapterPosition();
            Picasso.with(context)
                    .load(getItem(pos).getWinds().get(0).getImageUrl())
                    .fit().centerCrop().into(hl.getM_type());
            hl.getM_name().setText(getItem(pos).getCompleteName());
            hl.getM_date().setText
                    (Utils.getTimeSpan(
                            users.get(pos).getWinds().get(0).getSendDate()));
            hl.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOpeneable(getItem(position))) {
                        Utils.animateError(context, hl.getContainer());
                        return;
                    }
                    Intent i = new Intent(context, ShowTImeActivity.class);
                    i.putExtra("position", 2);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Snap.setTmpUser(getItem(position));
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size() + 1;
    }

    public User getItem(int position){
        return users.get(position);
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    private boolean isPositionFooter (int position) {
        return position == users.size ();
    }

    public void add(User item) {
        users.add(item);
        notifyItemInserted(users.size() - 1);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addAll(List<User> videos) {
        for (User user : videos) {
            add(user);
        }
    }

    public void onHeaderChanged(Wind wind){
        users.get(0).getWinds().add(wind);
        notifyItemChanged(0);
    }

    private boolean isOpeneable(User user){
        boolean open = false;
        for (Wind wi :
                user.getWinds()) {
            open = open || !wi.isOpened();
        }
        return open;
    }

    public void clearAll() {
        users.clear();
        users.add(Snap.getCurrent());
        notifyDataSetChanged();
    }
}
