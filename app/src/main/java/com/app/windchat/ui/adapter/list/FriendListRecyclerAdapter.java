package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.Utils;
import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.view.FriendListViewHolder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by banal_a on 10/12/2016.
 */

public class FriendListRecyclerAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

    private ArrayList<User> mItems;
    private Context context;
    private boolean isFriend;
    private String TYPE = "DEFAULT";

    public FriendListRecyclerAdapter(Context context, ArrayList<User> mItems, boolean isFriend) {
        this.mItems = mItems;
        this.context = context;
        this.isFriend = isFriend;
    }


    public FriendListRecyclerAdapter(Context context, ArrayList<User> mItems, boolean isFriend,
                                     String TYPE) {
        this.mItems = mItems;
        this.context = context;
        this.isFriend = isFriend;
        this.TYPE = TYPE;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_view_item_layout, parent, false);
        return new FriendListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendListViewHolder holder, int position) {
        final User user = getItem(holder.getAdapterPosition());
        if (!user.getPictureUrl().isEmpty())
            Picasso.with(context)
                    .load(user.getPictureUrl())
                    .error(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .into(holder.getUser_img());
        holder.getUser_name().setText(user.getCompleteName());
        String usname = "@" + user.getUsername();
        holder.getUser_uname().setText(usname);
        if (isFriend) {
            holder.getUser_delete().setVisibility(View.GONE);
            holder.getUser_add().setBackground(Utils.getDrawable(R.drawable.send_message_24dp));
        } else {
            if (TYPE.equals("DEFAULT")) {
                final JsonObject jsonObject = new JsonObject();
                holder.getUser_add().setImageResource(R.drawable.ic_add_white_24dp);
                holder.getUser_delete().setBackground(Utils.getDrawable(R.drawable.rounded_square_shape));
                holder.getUser_add().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jsonObject.addProperty("isAccepted", true);
                        Call<RestCode> call = new Api().getRestClient().accept(user.getId(), jsonObject);
                        call.enqueue(new Callback<RestCode>() {
                            @Override
                            public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                                if (response.isSuccessful()) {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    mItems.remove(user);
                                    Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestCode> call, Throwable t) {

                            }
                        });
                    }
                });

                holder.getUser_delete().setImageResource(R.drawable.ic_clear_white_24dp);
                holder.getUser_delete().setBackground(Utils.getDrawable(R.drawable.rounded_square_shape));
                holder.getUser_delete().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jsonObject.addProperty("isAccepted", false);
                        Call<RestCode> call = new Api().getRestClient().accept(user.getId(), jsonObject);
                        call.enqueue(new Callback<RestCode>() {
                            @Override
                            public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                                if (response.isSuccessful()) {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    mItems.remove(user);
                                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestCode> call, Throwable t) {

                            }
                        });
                    }
                });
            } else {
                holder.getUser_add().setImageResource(R.drawable.ic_add_white_24dp);
                holder.getUser_delete().setBackground(Utils.getDrawable(R.drawable.rounded_square_shape));
                holder.getUser_add().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JsonObject json = new JsonObject();
                        json.addProperty("userName", user.getUsername());
                        Call<RestCode> code = new Api().getRestClient().add_friend(json);
                        code.enqueue(new Callback<RestCode>() {
                            @Override
                            public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                                if (response.isSuccessful()) {
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    mItems.remove(user);
                                    Toast.makeText(context, "User added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error or already friend", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestCode> call, Throwable t) {

                            }
                        });
                    }
                });

                holder.getUser_delete().setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private User getItem(int position) {
        return mItems.get(position);
    }

    public void add(User item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addAll(List<User> videos) {
        for (User user : videos) {
            add(user);
        }
    }

    public void clearAll() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
