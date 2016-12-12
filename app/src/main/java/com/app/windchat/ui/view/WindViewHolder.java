package com.app.windchat.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.windchat.R;


/**
 * Created by banal_a on 08/12/2016.
 */

public class WindViewHolder extends RecyclerView.ViewHolder{

    private ImageView m_type;
    private TextView m_name;
    private TextView m_date;
    private LinearLayout m_container;

    public WindViewHolder(View v) {
        super(v);
        m_type = (ImageView) v.findViewById(R.id.m_type);
        m_date = (TextView) v.findViewById(R.id.m_date);
        m_name = (TextView) v.findViewById(R.id.m_name);
        m_container = (LinearLayout) v.findViewById(R.id.container);
    }

    public ImageView getM_type() {
        return m_type;
    }

    public TextView getM_name() {
        return m_name;
    }

    public TextView getM_date() {
        return m_date;
    }

    public LinearLayout getM_container() {
        return m_container;
    }
}
