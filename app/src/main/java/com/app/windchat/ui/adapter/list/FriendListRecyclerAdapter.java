package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.windchat.R;
import com.app.windchat.api.model.User;
import com.app.windchat.ui.view.FriendListViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banal_a on 10/12/2016.
 */

public class FriendListRecyclerAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

    private ArrayList<User> mItems;
    private Context context;
    private boolean isFriend;

    public FriendListRecyclerAdapter(Context context, ArrayList<User> mItems, boolean isFriend ) {
        this.mItems = mItems;
        this.context = context;
        this.isFriend = isFriend;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view_item_layout, parent, false);
        return new FriendListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        User user = getItem(holder.getAdapterPosition());
        if (!user.getPictureUrl().isEmpty())
        Picasso.with(context)
                .load(user.getPictureUrl())
                .error(R.mipmap.ic_launcher)
                .fit().centerCrop()
                .into(holder.getUser_img());
        holder.getUser_name().setText(user.getCompleteName());
        String usname = "@" + user.getUsername();
        holder.getUser_uname().setText(usname);
        if (isFriend){
            holder.getUser_delete().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private User getItem(int position){ return mItems.get(position);}

    public void add(User item) {
        mItems.add(item);
        notifyItemInserted(mItems.size()-1);
    }

    public void addAll(List<User> videos) {
        for (User user : videos) {
            add(user);
        }
    }

    public void clearAll(){
        mItems.clear();
        notifyDataSetChanged();
    }
}
