package com.app.windchat.ui.view;

import android.support.v4.widget.SlidingPaneLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private SlidingUpPanelLayout paneLayout;

    public SwipeGestureDetector(SlidingUpPanelLayout view) {
        paneLayout = view;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {

        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return onDown(e1);
            case 4:
                return true;
        }
        return false;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle >= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        paneLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        return super.onDown(e);
    }
}