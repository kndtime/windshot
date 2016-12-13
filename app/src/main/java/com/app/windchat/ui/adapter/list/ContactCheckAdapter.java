package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.app.windchat.R;
import com.app.windchat.api.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banal_a on 12/12/2016.
 */

public class ContactCheckAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> users;
    private LayoutInflater inflater;
    private ArrayList<Integer> ids;
    private onIdsChangedListener listener;

    public ContactCheckAdapter(Context context, ArrayList<User> users, onIdsChangedListener listener) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.ids = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View root = inflater.inflate(R.layout.friend_check_item_layout, null);
        TextView username = (TextView) root.findViewById(R.id.user_name);
        CircularImageView image = (CircularImageView) root.findViewById(R.id.user_img);
        CheckBox box = (CheckBox) root.findViewById(R.id.checkbox);
        username.setText(getItem(i).getUsername());
        Picasso.with(context)
                .load(getItem(i).getPictureUrl())
                .fit().centerInside().into(image);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    ids.remove(Integer.valueOf(getItem(i).getId()));
                }else{
                    ids.add(getItem(i).getId());
                }
                listener.onIdsChanged(ids);
            }
        });
        /*if (box.isChecked()){
            ids.remove(Integer.valueOf(getItem(i).getId()));
        }else{
            ids.add(getItem(i).getId());
        }*/
        return root;
    }

    public void add(User item) {
        users.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<User> videos) {
        for (User user : videos) {
            add(user);
        }
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public interface onIdsChangedListener{
        void onIdsChanged(ArrayList<Integer> ids);
    }
}
