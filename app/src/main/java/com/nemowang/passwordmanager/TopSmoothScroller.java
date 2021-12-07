package com.nemowang.passwordmanager;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;

public final class TopSmoothScroller extends LinearSmoothScroller {
    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }

    public TopSmoothScroller(@Nullable Context context) {
        super(context);
    }
}
