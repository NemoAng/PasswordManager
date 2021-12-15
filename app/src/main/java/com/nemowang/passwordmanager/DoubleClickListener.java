package com.nemowang.passwordmanager;


import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class DoubleClickListener implements OnClickListener {

    // The time in which the second tap should be done in order to qualify as
    // a double click
    private static final long DEFAULT_DOUBLE_SPAN = 150;
    private final long doubleClickSpanInMS;
    private long timestampLastClick;
    private boolean doubleClicked = false;
//    private int clickedCnt = 0;

    public DoubleClickListener() {
        doubleClickSpanInMS = DEFAULT_DOUBLE_SPAN;
        timestampLastClick = 0;
    }

    public DoubleClickListener(long doubleClickSpanInMS) {
        this.doubleClickSpanInMS = doubleClickSpanInMS;
        timestampLastClick = 0;
    }

    @Override
    public void onClick(View v) {
        doubleClicked = false;
//        clickedCnt++;

//        v.removeCallbacks(null);

        if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickSpanInMS) {
            doubleClicked = true;
//            if(clickedCnt < 3)
                onDoubleClick(v);
        }

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!doubleClicked){
                    onSingleClick(v);
//                    clickedCnt = 0;
                }
            }
        }, doubleClickSpanInMS);
        timestampLastClick = SystemClock.elapsedRealtime();
    }

    public abstract void onDoubleClick(View v);
    public abstract void onSingleClick(View v);
}
