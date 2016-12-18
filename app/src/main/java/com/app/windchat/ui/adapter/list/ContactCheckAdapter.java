package com.app.windchat.ui.adapter.list;

import android.content.Context;
import android.support.v7.widget.CardView;
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
    private ArrayList<User> selected;
    private onIdsChangedListener listener;

    public ContactCheckAdapter(Context context, ArrayList<User> users, onIdsChangedListener listener) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.ids = new ArrayList<>();
        this.selected = new ArrayList<>();
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

        final CardView container = (CardView) root.findViewById(R.id.container);
        CircularImageView image = (CircularImageView) root.findViewById(R.id.user_img);
        final CheckBox box = (CheckBox) root.findViewById(R.id.checkbox);
        box.setChecked(getItem(i).isSelected());

        if (box.isChecked()){
            container.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            container.setCardBackgroundColor(null);
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                box.setChecked(!box.isChecked());
                notifyDataSetChanged();
            }
        });


        username.setText(getItem(i).getUsername());
        Picasso.with(context)
                .load(getItem(i).getPictureUrl())
                .fit().centerInside().into(image);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    getItem(i).setSelected(false);
                    selected.remove((getItem(i)));
                }else{
                    getItem(i).setSelected(true);
                    if (!selected.contains(getItem(i)))
                        selected.add(getItem(i));
                }
                listener.onIdsChanged(getIds(), getNames());
            }
        });
        box.setVisibility(View.GONE);
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

    public String getNames(){
        String names = "";
        for (User user: selected
                ) {
           names = names + user.getUsername() +", ";
        }
        names = names + "...";
        return names;
    }

    public ArrayList<Integer> getIds(){
        ArrayList<Integer> ids=  new ArrayList<>();
        for (User user: selected
             ) {
            if (!ids.contains(user.getId()))
                ids.add(user.getId());
        }
        return ids;
    }

    public interface onIdsChangedListener{
        void onIdsChanged(ArrayList<Integer> ids, String names);
    }
}
