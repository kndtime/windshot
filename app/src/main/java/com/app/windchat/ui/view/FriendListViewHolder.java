package com.app.windchat.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.windchat.R;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by banal_a on 10/12/2016.
 */

public class FriendListViewHolder extends RecyclerView.ViewHolder{

    private ImageView user_add;
    private ImageView user_delete;
    private TextView user_name, user_uname;
    private CircularImageView user_img;

    public FriendListViewHolder(View v) {
        super(v);
        user_img = (CircularImageView) v.findViewById(R.id.user_img);
        user_add = (ImageView) v.findViewById(R.id.user_add);
        user_delete = (ImageView) v.findViewById(R.id.user_delete);
        user_name = (TextView) v.findViewById(R.id.user_name);
        user_uname = (TextView) v.findViewById(R.id.user_uname);
    }

    public ImageView getUser_add() {
        return user_add;
    }

    public ImageView getUser_delete() {
        return user_delete;
    }

    public TextView getUser_name() {
        return user_name;
    }

    public CircularImageView getUser_img() {
        return user_img;
    }

    public TextView getUser_uname() {
        return user_uname;
    }
}
