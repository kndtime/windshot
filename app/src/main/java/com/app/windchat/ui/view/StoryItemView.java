package com.app.windchat.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.windchat.R;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by banal_a on 18/12/2016.
 */

public class StoryItemView extends RecyclerView.ViewHolder {

    private LinearLayout container;
    private CircularImageView m_type;

    private TextView m_name;
    private TextView m_date;

    public StoryItemView(View v) {
        super(v);
        container = (LinearLayout) v.findViewById(R.id.container);
        m_date = (TextView) v.findViewById(R.id.m_date);
        m_name = (TextView) v.findViewById(R.id.m_name);
        m_type = (CircularImageView) v.findViewById(R.id.m_type);
    }

    public LinearLayout getContainer() {
        return container;
    }

    public CircularImageView getM_type() {
        return m_type;
    }

    public TextView getM_name() {
        return m_name;
    }

    public TextView getM_date() {
        return m_date;
    }
}
